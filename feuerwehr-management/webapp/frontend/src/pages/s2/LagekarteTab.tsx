import { useEffect, useMemo, useRef, useState } from 'react';
import { MagnifyingGlassMinusIcon, MagnifyingGlassPlusIcon, ArrowsPointingOutIcon, TrashIcon } from '@heroicons/react/24/outline';
import { s2Api, type GeoTreffer, type Pt, type S2Abschnitt, type S2LageState, type S2Objekt } from '../../api/s2';
import { S2_ZEICHEN } from '../../config/s2Zeichen';
import { Button } from '../../components/ui/Button';
import { useToastStore } from '../../components/ui/Toast';
import { cn } from '../../utils/cn';
import { FLAECHEN, KARTE, LINIEN, fehlertext, formatGitter, imAusschnitt, parseGitter, pxToUtm, utmToPx } from '../../utils/s2';
import { useItemMutations } from './useS2';

type Tool = 'auswahl' | 'zeichen' | 'flaeche' | 'linie' | 'ea';
interface View { x: number; y: number; w: number; h: number }
const FULL: View = { x: 0, y: 0, w: KARTE.width, h: KARTE.height };
const zeichenUrl = (id: string) => `/s2/zeichen/${encodeURIComponent(id)}.png`;
const label = (id: string) => S2_ZEICHEN.find((z) => z.id === id)?.label ?? id;

function centroid(pts: Pt[]): Pt {
  return [pts.reduce((a, p) => a + p[0], 0) / pts.length, pts.reduce((a, p) => a + p[1], 0) / pts.length];
}

/**
 * Lagekarte im Browser auf dem Kartenbild der PowerPoint-Vorlage (LVGL 2026, UTM-Gitter 1 km).
 * Ebenengerecht wie im RV-Beispiel 03a: EA-Grenzen, Schadensflächen, Linien und Markierungen auf
 * der Karte; Gefahren-/Maßnahmen-/Stellenzeichen im Export in den Randkästchen mit Pfeil zum Ort.
 */
export function LagekarteTab({ lage }: { lage: S2LageState }) {
  const obj = useItemMutations<S2Objekt>(lage.id, 'objekte');
  const ab = useItemMutations<S2Abschnitt>(lage.id, 'abschnitte');
  const addToast = useToastStore((s) => s.addToast);
  const svgRef = useRef<SVGSVGElement>(null);
  const [view, setView] = useState<View>(FULL);
  const [tool, setTool] = useState<Tool>('auswahl');
  const [zeichenId, setZeichenId] = useState('STELLE_02');
  const [randkaestchen, setRandkaestchen] = useState(true);
  const [text, setText] = useState('');
  const [suche, setSuche] = useState('');
  const [flaechenTyp, setFlaechenTyp] = useState('ueberschwemmt');
  const [linienTyp, setLinienTyp] = useState('rettungsweg');
  const [eaId, setEaId] = useState<number | null>(null);
  const [draft, setDraft] = useState<Pt[]>([]);
  const [selected, setSelected] = useState<number | null>(null);
  const [verschieben, setVerschieben] = useState(false);
  const [cursor, setCursor] = useState<Pt | null>(null);
  const [ortText, setOrtText] = useState('');
  const [treffer, setTreffer] = useState<GeoTreffer[] | null>(null);
  const [marker, setMarker] = useState<Pt | null>(null);
  const drag = useRef<{ sx: number; sy: number; view: View; moved: boolean } | null>(null);

  // React registers wheel listeners as passive: without this the page scrolls while zooming
  useEffect(() => {
    const el = svgRef.current;
    if (!el) return undefined;
    const stop = (e: WheelEvent) => e.preventDefault();
    el.addEventListener('wheel', stop, { passive: false });
    return () => el.removeEventListener('wheel', stop);
  }, []);

  const sel = lage.objekte.find((o) => o.id === selected) ?? null;
  const symbolPx = Math.max(40, view.w / 32);
  const palette = useMemo(() => {
    const q = suche.trim().toLowerCase();
    return S2_ZEICHEN.filter((z) => !q || z.label.toLowerCase().includes(q) || z.id.toLowerCase().includes(q) || z.gruppe.toLowerCase().includes(q));
  }, [suche]);

  const toSvg = (clientX: number, clientY: number): Pt => {
    const svg = svgRef.current!;
    const p = svg.createSVGPoint();
    p.x = clientX; p.y = clientY;
    const r = p.matrixTransform(svg.getScreenCTM()!.inverse());
    return [r.x, r.y];
  };

  const zoom = (factor: number, center?: Pt) => {
    setView((v) => {
      const w = Math.min(KARTE.width, Math.max(300, v.w * factor));
      const h = (w / KARTE.width) * KARTE.height;
      const [cx, cy] = center ?? [v.x + v.w / 2, v.y + v.h / 2];
      const x = Math.min(Math.max(0, cx - ((cx - v.x) * w) / v.w), KARTE.width - w);
      const y = Math.min(Math.max(0, cy - ((cy - v.y) * h) / v.h), KARTE.height - h);
      return { x, y, w, h };
    });
  };

  const zeigeOrt = (pt: Pt) => {
    const [px, py] = utmToPx(pt);
    const w = Math.min(view.w, 1200);
    const h = (w / KARTE.width) * KARTE.height;
    setView({ x: Math.min(Math.max(0, px - w / 2), KARTE.width - w), y: Math.min(Math.max(0, py - h / 2), KARTE.height - h), w, h });
    setMarker(pt);
  };

  /** A place was picked (map click, grid value or address): what happens depends on the tool. */
  const ortGewaehlt = (pt: Pt) => {
    if (!imAusschnitt(pt)) { addToast('Ort liegt außerhalb des Kartenausschnitts'); return; }
    if (verschieben && sel) {
      obj.update.mutate({ item: sel, changes: { punkte: [pt] } });
      setVerschieben(false);
      return;
    }
    if (tool === 'zeichen') {
      obj.create.mutate({ art: 'zeichen', typ: zeichenId, punkte: [pt], randkaestchen, text: text || null } as Partial<S2Objekt>,
        { onSuccess: () => setText('') });
    } else if (tool === 'flaeche' || tool === 'linie' || tool === 'ea') {
      setDraft((d) => [...d, pt]);
    }
  };

  const fertig = () => {
    if (tool === 'ea') {
      const a = lage.abschnitte.find((x) => x.id === eaId);
      if (!a) { addToast('Bitte zuerst den Einsatzabschnitt wählen'); return; }
      ab.update.mutate({ item: a, changes: { flaeche: draft } }, { onSuccess: () => setDraft([]) });
    } else {
      obj.create.mutate({ art: tool === 'flaeche' ? 'flaeche' : 'linie', typ: tool === 'flaeche' ? flaechenTyp : linienTyp, punkte: draft, text: text || null } as Partial<S2Objekt>,
        { onSuccess: () => { setDraft([]); setText(''); } });
    }
  };

  const minPunkte = tool === 'linie' ? 2 : 3;

  const sucheOrt = async () => {
    const g = parseGitter(ortText);
    if (g) { setTreffer(null); zeigeOrt(g); return; }
    try {
      const [strasse, ortsteil] = ortText.split(',').map((s) => s.trim());
      setTreffer((await s2Api.geo(strasse, ortsteil)).data.data);
    } catch (err) {
      addToast(await fehlertext(err));
    }
  };

  const wechsleTool = (t: Tool) => { setTool(t); setDraft([]); setVerschieben(false); if (t !== 'auswahl') setSelected(null); };

  return (
    <div className="flex flex-col xl:flex-row gap-4">
      <div className="flex-1 min-w-0 space-y-2">
        <div className="flex flex-wrap items-center gap-2 text-sm">
          <Button size="sm" variant="secondary" aria-label="Vergrößern" onClick={() => zoom(0.7)}><MagnifyingGlassPlusIcon className="h-4 w-4" /></Button>
          <Button size="sm" variant="secondary" aria-label="Verkleinern" onClick={() => zoom(1 / 0.7)}><MagnifyingGlassMinusIcon className="h-4 w-4" /></Button>
          <Button size="sm" variant="secondary" aria-label="Ganze Karte" onClick={() => setView(FULL)}><ArrowsPointingOutIcon className="h-4 w-4" /></Button>
          <span className="text-gray-500 tabular-nums" data-testid="s2-cursor">{cursor ? `Gitter ${formatGitter(cursor)} km` : 'Mausrad = Zoom, Ziehen = Verschieben'}</span>
          {(draft.length > 0 || verschieben) && (
            <span className="ml-auto text-amber-700">{verschieben ? 'Neuen Ort auf der Karte anklicken' : `${draft.length} Punkt(e) gesetzt`}</span>
          )}
        </div>
        <div className="border border-gray-300 rounded-md overflow-hidden bg-gray-100">
          <svg ref={svgRef} data-testid="s2-karte" viewBox={`${view.x} ${view.y} ${view.w} ${view.h}`}
            className={cn('w-full h-auto block select-none touch-none', tool === 'auswahl' && !verschieben ? 'cursor-grab' : 'cursor-crosshair')}
            onWheel={(e) => { zoom(e.deltaY < 0 ? 0.85 : 1 / 0.85, toSvg(e.clientX, e.clientY)); }}
            onPointerDown={(e) => { drag.current = { sx: e.clientX, sy: e.clientY, view, moved: false }; (e.target as Element).setPointerCapture?.(e.pointerId); }}
            onPointerMove={(e) => {
              setCursor(pxToUtm(toSvg(e.clientX, e.clientY)));
              const d = drag.current;
              if (!d) return;
              if (Math.abs(e.clientX - d.sx) + Math.abs(e.clientY - d.sy) > 6) d.moved = true;
              if (d.moved) {
                const scale = d.view.w / svgRef.current!.clientWidth;
                const x = Math.min(Math.max(0, d.view.x - (e.clientX - d.sx) * scale), KARTE.width - d.view.w);
                const y = Math.min(Math.max(0, d.view.y - (e.clientY - d.sy) * scale), KARTE.height - d.view.h);
                setView({ ...d.view, x, y });
              }
            }}
            onPointerUp={(e) => {
              const d = drag.current;
              drag.current = null;
              if (!d || d.moved) return;
              const target = (e.target as Element).closest('[data-objekt]');
              if (tool === 'auswahl' && !verschieben) {
                setSelected(target ? Number(target.getAttribute('data-objekt')) : null);
                return;
              }
              ortGewaehlt(pxToUtm(toSvg(e.clientX, e.clientY)));
            }}
            onPointerLeave={() => setCursor(null)}>
            <image href="/s2/lagekarte-heusweiler.jpg" x={0} y={0} width={KARTE.width} height={KARTE.height} preserveAspectRatio="none" />

            {lage.abschnitte.filter((a) => a.flaeche).map((a) => {
              const pts = a.flaeche!.map(utmToPx);
              const top = pts.reduce((m, p) => (p[1] < m[1] ? p : m), pts[0]);
              const [cx, cy] = centroid(pts);
              return (
                <g key={`ea-${a.id}`}>
                  <polygon points={pts.map((p) => p.join(',')).join(' ')} fill="none" stroke="#000" strokeWidth={8} strokeDasharray="40 12 6 12 6 12" />
                  <text x={top[0]} y={top[1] - 14} fontSize={46} fontWeight="bold" textAnchor="middle" fontFamily="Arial">EA {a.nr}: {a.name}</text>
                  <circle cx={cx} cy={cy} r={34} fill="#FFED00" stroke="#000" strokeWidth={4} />
                  <text x={cx} y={cy + 16} fontSize={44} fontWeight="bold" textAnchor="middle" fontFamily="Arial">{a.nr}</text>
                </g>
              );
            })}

            {lage.objekte.filter((o) => o.art === 'flaeche').map((o) => {
              const c = FLAECHEN[o.typ]?.color ?? '#888';
              const pts = o.punkte.map(utmToPx);
              return (
                <g key={o.id} data-objekt={o.id}>
                  <polygon points={pts.map((p) => p.join(',')).join(' ')} fill={c} fillOpacity={0.45} stroke={selected === o.id ? '#000' : c} strokeWidth={selected === o.id ? 10 : 5} />
                  {o.text && <text x={centroid(pts)[0]} y={centroid(pts)[1]} fontSize={34} textAnchor="middle" fontFamily="Arial">{o.text}</text>}
                </g>
              );
            })}
            {lage.objekte.filter((o) => o.art === 'linie').map((o) => (
              <polyline key={o.id} data-objekt={o.id} points={o.punkte.map(utmToPx).map((p) => p.join(',')).join(' ')} fill="none"
                stroke={LINIEN[o.typ]?.color ?? '#888'} strokeWidth={selected === o.id ? 22 : 14} strokeLinecap="round" />
            ))}
            {lage.objekte.filter((o) => o.art === 'zeichen').map((o) => {
              const [x, y] = utmToPx(o.punkte[0]);
              const s = symbolPx;
              return (
                <g key={o.id} data-objekt={o.id}>
                  <title>{`${label(o.typ)}${o.text ? ` – ${o.text}` : ''}${o.randkaestchen ? ' (im Export im Randkästchen mit Pfeil)' : ''}`}</title>
                  <rect x={x - s * 0.6} y={y - s * 0.6} width={s * 1.2} height={s * 1.2} rx={s * 0.15} fill="#fff" fillOpacity={0.85}
                    stroke={selected === o.id ? '#dc2626' : '#666'} strokeWidth={selected === o.id ? s * 0.08 : s * 0.03}
                    strokeDasharray={o.randkaestchen ? `${s * 0.12} ${s * 0.08}` : undefined} />
                  <image href={zeichenUrl(o.typ)} x={x - s / 2} y={y - s / 2} width={s} height={s} />
                  {o.text && <text x={x} y={y + s * 0.6 + s * 0.35} fontSize={s * 0.32} textAnchor="middle" fontFamily="Arial"
                    stroke="#fff" strokeWidth={s * 0.08} paintOrder="stroke">{o.text}</text>}
                </g>
              );
            })}

            {draft.length > 0 && (() => {
              const pts = draft.map(utmToPx);
              const color = tool === 'flaeche' ? FLAECHEN[flaechenTyp].color : tool === 'linie' ? LINIEN[linienTyp].color : '#000';
              return (
                <g pointerEvents="none">
                  <polyline points={[...pts, ...(tool !== 'linie' && pts.length > 2 ? [pts[0]] : [])].map((p) => p.join(',')).join(' ')}
                    fill={tool === 'flaeche' ? color : 'none'} fillOpacity={0.3} stroke={color} strokeWidth={8} strokeDasharray="20 10" />
                  {pts.map((p, i) => <circle key={i} cx={p[0]} cy={p[1]} r={12} fill={color} stroke="#fff" strokeWidth={3} />)}
                </g>
              );
            })()}
            {marker && (() => {
              const [x, y] = utmToPx(marker);
              return <g pointerEvents="none"><circle cx={x} cy={y} r={28} fill="none" stroke="#dc2626" strokeWidth={8} /><circle cx={x} cy={y} r={5} fill="#dc2626" /></g>;
            })()}
          </svg>
        </div>
        <p className="text-xs text-gray-500">
          Gestrichelt umrandete Zeichen erscheinen im Export groß im Randkästchen mit schwarzem Pfeil zum Ort, die anderen direkt auf der Karte.
          Keine Kräfte auf die Karte – die stehen im Lagebericht. Kartengrundlage LVGL 2026.
        </p>
      </div>

      <div className="xl:w-80 shrink-0 space-y-4">
        <div className="bg-white rounded-lg border border-gray-200 p-3 space-y-3">
          <div className="grid grid-cols-3 gap-1">
            {([['auswahl', 'Auswahl'], ['zeichen', 'Zeichen'], ['flaeche', 'Fläche'], ['linie', 'Linie'], ['ea', 'EA-Grenze']] as [Tool, string][]).map(([t, l]) => (
              <button key={t} onClick={() => wechsleTool(t)}
                className={cn('rounded px-2 py-1.5 text-sm border', tool === t ? 'bg-red-600 text-white border-red-600' : 'bg-white border-gray-300 hover:bg-gray-50')}>
                {l}
              </button>
            ))}
          </div>

          {tool === 'zeichen' && (
            <div className="space-y-2">
              <input className="w-full border border-gray-300 rounded-md px-2 py-1 text-sm" placeholder="Zeichen suchen (z. B. Pumpen, Brand)"
                aria-label="Zeichen suchen" value={suche} onChange={(e) => setSuche(e.target.value)} />
              <div className="grid grid-cols-5 gap-1 max-h-56 overflow-y-auto border border-gray-100 rounded p-1">
                {palette.map((z) => (
                  <button key={z.id} title={`${z.label} (${z.id})`} aria-label={z.label}
                    onClick={() => { setZeichenId(z.id); setRandkaestchen(!z.id.startsWith('MARKIERUNG')); }}
                    className={cn('aspect-square rounded border p-0.5 bg-white', zeichenId === z.id ? 'border-red-600 ring-2 ring-red-300' : 'border-gray-200')}>
                    <img src={zeichenUrl(z.id)} alt="" className="w-full h-full object-contain" />
                  </button>
                ))}
              </div>
              <p className="text-sm font-medium">{label(zeichenId)}</p>
              <label className="flex items-center gap-2 text-sm">
                <input type="checkbox" checked={randkaestchen} onChange={(e) => setRandkaestchen(e.target.checked)} />
                im Randkästchen mit Pfeil (RV-Beispiel)
              </label>
              <input className="w-full border border-gray-300 rounded-md px-2 py-1 text-sm" placeholder="Text (optional, ≤ 3 Wörter)"
                aria-label="Text zum Zeichen" value={text} maxLength={100} onChange={(e) => setText(e.target.value)} />
              <p className="text-xs text-gray-500">Ort auf der Karte anklicken oder unten eingeben.</p>
            </div>
          )}
          {tool === 'flaeche' && (
            <select aria-label="Flächentyp" className="w-full border border-gray-300 rounded-md px-2 py-1 text-sm" value={flaechenTyp} onChange={(e) => setFlaechenTyp(e.target.value)}>
              {Object.entries(FLAECHEN).map(([k, v]) => <option key={k} value={k}>{v.label}</option>)}
            </select>
          )}
          {tool === 'linie' && (
            <select aria-label="Linientyp" className="w-full border border-gray-300 rounded-md px-2 py-1 text-sm" value={linienTyp} onChange={(e) => setLinienTyp(e.target.value)}>
              {Object.entries(LINIEN).map(([k, v]) => <option key={k} value={k}>{v.label}</option>)}
            </select>
          )}
          {tool === 'ea' && (
            lage.abschnitte.length === 0 ? <p className="text-sm text-amber-700">Zuerst unter „Abschnitte & Kräfte“ einen Einsatzabschnitt anlegen.</p> : (
              <select aria-label="Einsatzabschnitt" className="w-full border border-gray-300 rounded-md px-2 py-1 text-sm" value={eaId ?? ''} onChange={(e) => setEaId(Number(e.target.value) || null)}>
                <option value="">Einsatzabschnitt wählen …</option>
                {lage.abschnitte.map((a) => <option key={a.id} value={a.id}>EA {a.nr}: {a.name}{a.flaeche ? ' (Grenze ersetzen)' : ''}</option>)}
              </select>
            )
          )}
          {(tool === 'flaeche' || tool === 'linie' || tool === 'ea') && (
            <div className="space-y-2">
              {tool === 'flaeche' && (
                <input className="w-full border border-gray-300 rounded-md px-2 py-1 text-sm" placeholder="Beschriftung (optional)" aria-label="Beschriftung"
                  value={text} maxLength={100} onChange={(e) => setText(e.target.value)} />
              )}
              <p className="text-xs text-gray-500">Ecken nacheinander auf der Karte anklicken (oder Gitterwerte eingeben), dann „Fertig“.</p>
              <div className="flex gap-2">
                <Button size="sm" variant="primary" disabled={draft.length < minPunkte} onClick={fertig}
                  loading={obj.create.isPending || ab.update.isPending}>Fertig</Button>
                <Button size="sm" variant="secondary" disabled={draft.length === 0} onClick={() => setDraft((d) => d.slice(0, -1))}>Punkt zurück</Button>
                <Button size="sm" variant="ghost" disabled={draft.length === 0} onClick={() => setDraft([])}>Verwerfen</Button>
              </div>
            </div>
          )}

          <div className="border-t border-gray-100 pt-3 space-y-2">
            <label className="text-xs font-medium text-gray-600" htmlFor="s2-ort">Ort: Gitter (351,2/5466,8) oder Adresse („Straße Nr, Ortsteil“)</label>
            <div className="flex gap-2">
              <input id="s2-ort" className="flex-1 min-w-0 border border-gray-300 rounded-md px-2 py-1 text-sm" value={ortText}
                onChange={(e) => setOrtText(e.target.value)} onKeyDown={(e) => { if (e.key === 'Enter' && ortText.trim()) sucheOrt(); }} />
              <Button size="sm" variant="secondary" disabled={!ortText.trim()} onClick={sucheOrt}>Suchen</Button>
            </div>
            {marker && (tool !== 'auswahl' || verschieben) && (
              <Button size="sm" variant="primary" onClick={() => ortGewaehlt(marker)}>
                {verschieben ? 'Hierher verschieben' : tool === 'zeichen' ? 'Zeichen hier setzen' : 'Punkt hier setzen'} ({formatGitter(marker)})
              </Button>
            )}
            {treffer && (
              <ul className="text-sm space-y-1">
                {treffer.map((t, i) => (
                  <li key={i}>
                    {t.utm ? (
                      <button className="text-left text-blue-700 hover:underline" onClick={() => zeigeOrt(t.utm!)}>
                        {t.strasse} {t.hausnr}, {t.ortsteil}
                        <span className="text-xs text-gray-500"> – {t.genauigkeit === 'HAUSNR' ? 'Hausnummer' : t.genauigkeit === 'STRASSENMITTE' ? 'Straßenmitte' : t.hinweis}</span>
                        {t.hinweis?.includes('mehrdeutig') && <span className="text-xs text-amber-700"> (mehrdeutig)</span>}
                      </button>
                    ) : (
                      <span className="text-amber-700">
                        Nicht gefunden.{t.vorschlaege?.length ? ` Meinten Sie: ${t.vorschlaege.join(', ')}?` : ''} Ort nicht raten – Gitterwert erfragen.
                      </span>
                    )}
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>

        {sel && (
          <div className="bg-white rounded-lg border border-gray-200 p-3 space-y-2" data-testid="s2-auswahl">
            <div className="flex items-center gap-2">
              {sel.art === 'zeichen' && <img src={zeichenUrl(sel.typ)} alt="" className="h-10 w-10 object-contain" />}
              <div className="min-w-0">
                <p className="font-medium text-sm">{sel.art === 'zeichen' ? label(sel.typ) : sel.art === 'flaeche' ? `Fläche: ${FLAECHEN[sel.typ]?.label}` : `Linie: ${LINIEN[sel.typ]?.label}`}</p>
                <p className="text-xs text-gray-500">{formatGitter(sel.punkte[0])}{sel.updatedBy && ` · ${sel.updatedBy}`}</p>
              </div>
            </div>
            {sel.art !== 'linie' && (
              <input key={`${sel.id}-${sel.version}`} className="w-full border border-gray-300 rounded-md px-2 py-1 text-sm" defaultValue={sel.text ?? ''}
                aria-label="Text" placeholder="Text" maxLength={100}
                onBlur={(e) => { if (e.target.value !== (sel.text ?? '')) obj.update.mutate({ item: sel, changes: { text: e.target.value } }); }} />
            )}
            {sel.art === 'zeichen' && (
              <label className="flex items-center gap-2 text-sm">
                <input type="checkbox" checked={sel.randkaestchen} onChange={(e) => obj.update.mutate({ item: sel, changes: { randkaestchen: e.target.checked } })} />
                im Randkästchen mit Pfeil
              </label>
            )}
            <div className="flex gap-2">
              {sel.art === 'zeichen' && <Button size="sm" variant="secondary" onClick={() => setVerschieben(true)}>Verschieben</Button>}
              <Button size="sm" variant="danger" onClick={() => { obj.remove.mutate(sel); setSelected(null); }}>Löschen</Button>
            </div>
          </div>
        )}

        <div className="bg-white rounded-lg border border-gray-200 p-3">
          <p className="text-sm font-medium mb-2">Auf der Karte ({lage.objekte.length + lage.abschnitte.filter((a) => a.flaeche).length})</p>
          <ul className="text-sm divide-y divide-gray-100 max-h-72 overflow-y-auto">
            {lage.abschnitte.filter((a) => a.flaeche).map((a) => (
              <li key={`ea-${a.id}`} className="py-1 flex items-center gap-2">
                <span className="flex-1">EA {a.nr}: {a.name}</span>
                <button aria-label={`Grenze EA ${a.nr} entfernen`} className="text-gray-400 hover:text-red-600"
                  onClick={() => ab.update.mutate({ item: a, changes: { flaeche: null } })}><TrashIcon className="h-4 w-4" /></button>
              </li>
            ))}
            {lage.objekte.map((o) => (
              <li key={o.id} className={cn('py-1 flex items-center gap-2 cursor-pointer', selected === o.id && 'bg-red-50')}
                onClick={() => { setTool('auswahl'); setSelected(o.id); zeigeOrt(o.punkte[0]); }}>
                {o.art === 'zeichen'
                  ? <img src={zeichenUrl(o.typ)} alt="" className="h-5 w-5 object-contain" />
                  : <span className="h-3 w-5 rounded-sm" style={{ background: (o.art === 'flaeche' ? FLAECHEN : LINIEN)[o.typ]?.color }} />}
                <span className="flex-1 truncate">{o.art === 'zeichen' ? label(o.typ) : (o.art === 'flaeche' ? FLAECHEN : LINIEN)[o.typ]?.label}{o.text && ` – ${o.text}`}</span>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}
