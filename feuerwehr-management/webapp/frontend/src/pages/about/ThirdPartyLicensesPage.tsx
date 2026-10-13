import React from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeftIcon } from '@heroicons/react/24/outline';

interface Library {
  name: string;
  version: string;
  license: string;
  url: string;
  category: 'frontend' | 'backend';
}

// Versions as installed via package-lock.json (Release 1.4.0). Keep in sync when
// updating dependencies; build tools are listed because they ship code into the bundle.
const libraries: Library[] = [
  // Frontend
  { name: 'React', version: '18.3.1', license: 'MIT', url: 'https://react.dev', category: 'frontend' },
  { name: 'React DOM', version: '18.3.1', license: 'MIT', url: 'https://react.dev', category: 'frontend' },
  { name: 'React Router DOM', version: '7.18.4', license: 'MIT', url: 'https://reactrouter.com', category: 'frontend' },
  { name: '@headlessui/react', version: '1.7.19', license: 'MIT', url: 'https://headlessui.com', category: 'frontend' },
  { name: '@heroicons/react', version: '2.2.0', license: 'MIT', url: 'https://heroicons.com', category: 'frontend' },
  { name: '@tanstack/react-query', version: '5.103.2', license: 'MIT', url: 'https://tanstack.com/query', category: 'frontend' },
  { name: 'axios', version: '1.20.0', license: 'MIT', url: 'https://axios-http.com', category: 'frontend' },
  { name: 'zustand', version: '5.0.15', license: 'MIT', url: 'https://github.com/pmndrs/zustand', category: 'frontend' },
  { name: 'date-fns', version: '4.4.0', license: 'MIT', url: 'https://date-fns.org', category: 'frontend' },
  { name: 'jsPDF', version: '4.2.1', license: 'MIT', url: 'https://github.com/parallax/jsPDF', category: 'frontend' },
  { name: 'jspdf-autotable', version: '5.0.8', license: 'MIT', url: 'https://github.com/simonbengtsson/jsPDF-AutoTable', category: 'frontend' },
  { name: 'qrcode', version: '1.5.4', license: 'MIT', url: 'https://github.com/soldair/node-qrcode', category: 'frontend' },
  { name: 'JsBarcode', version: '3.12.3', license: 'MIT', url: 'https://github.com/lindell/JsBarcode', category: 'frontend' },
  { name: 'react-hook-form', version: '7.88.0', license: 'MIT', url: 'https://react-hook-form.com', category: 'frontend' },
  { name: '@hookform/resolvers', version: '3.10.0', license: 'MIT', url: 'https://github.com/react-hook-form/resolvers', category: 'frontend' },
  { name: 'zod', version: '3.25.76', license: 'MIT', url: 'https://zod.dev', category: 'frontend' },
  { name: 'Recharts', version: '3.10.1', license: 'MIT', url: 'https://recharts.github.io', category: 'frontend' },
  { name: 'clsx', version: '2.1.1', license: 'MIT', url: 'https://github.com/lukeed/clsx', category: 'frontend' },
  { name: 'tailwind-merge', version: '2.6.1', license: 'MIT', url: 'https://github.com/dcastil/tailwind-merge', category: 'frontend' },
  { name: 'Tailwind CSS', version: '4.3.3', license: 'MIT', url: 'https://tailwindcss.com', category: 'frontend' },
  { name: 'Vite', version: '6.4.4', license: 'MIT', url: 'https://vite.dev', category: 'frontend' },
  { name: 'TypeScript', version: '5.9.3', license: 'Apache-2.0', url: 'https://www.typescriptlang.org', category: 'frontend' },
  // Backend
  { name: 'Node.js', version: '≥ 20', license: 'MIT', url: 'https://nodejs.org', category: 'backend' },
  { name: 'Express', version: '4.22.3', license: 'MIT', url: 'https://expressjs.com', category: 'backend' },
  { name: 'Prisma', version: '6.19.3', license: 'Apache-2.0', url: 'https://www.prisma.io', category: 'backend' },
  { name: 'bcryptjs', version: '2.4.3', license: 'MIT', url: 'https://github.com/dcodeIO/bcrypt.js', category: 'backend' },
  { name: 'compression', version: '1.8.2', license: 'MIT', url: 'https://github.com/expressjs/compression', category: 'backend' },
  { name: 'cookie-parser', version: '1.4.7', license: 'MIT', url: 'https://github.com/expressjs/cookie-parser', category: 'backend' },
  { name: 'cors', version: '2.8.6', license: 'MIT', url: 'https://github.com/expressjs/cors', category: 'backend' },
  { name: 'dotenv', version: '16.6.1', license: 'BSD-2-Clause', url: 'https://github.com/motdotla/dotenv', category: 'backend' },
  { name: 'express-rate-limit', version: '7.5.1', license: 'MIT', url: 'https://github.com/express-rate-limit/express-rate-limit', category: 'backend' },
  { name: 'express-validator', version: '7.3.2', license: 'MIT', url: 'https://express-validator.github.io', category: 'backend' },
  { name: 'helmet', version: '8.3.0', license: 'MIT', url: 'https://helmetjs.github.io', category: 'backend' },
  { name: 'jsonwebtoken', version: '9.0.3', license: 'MIT', url: 'https://github.com/auth0/node-jsonwebtoken', category: 'backend' },
  { name: 'morgan', version: '1.12.1', license: 'MIT', url: 'https://github.com/expressjs/morgan', category: 'backend' },
  { name: 'multer', version: '2.4.0', license: 'MIT', url: 'https://github.com/expressjs/multer', category: 'backend' },
  { name: 'Nodemailer', version: '10.0.16', license: 'MIT-0', url: 'https://nodemailer.com', category: 'backend' },
  { name: 'winston', version: '3.19.0', license: 'MIT', url: 'https://github.com/winstonjs/winston', category: 'backend' },
  { name: 'winston-daily-rotate-file', version: '5.0.0', license: 'MIT', url: 'https://github.com/winstonjs/winston-daily-rotate-file', category: 'backend' },
  { name: 'docxtemplater', version: '3.71.0', license: 'MIT', url: 'https://docxtemplater.com', category: 'backend' },
  { name: 'PizZip', version: '3.3.0', license: 'MIT OR GPL-3.0', url: 'https://github.com/open-xml-templating/pizzip', category: 'backend' },
  { name: 'ExcelJS', version: '4.4.0', license: 'MIT', url: 'https://github.com/exceljs/exceljs', category: 'backend' },
  { name: 'LibreOffice (PDF-Konvertierung, separat installiert)', version: '–', license: 'MPL-2.0', url: 'https://www.libreoffice.org', category: 'backend' },
];

const licenseBadgeColor: Record<string, string> = {
  'MIT': 'bg-green-100 text-green-700',
  'MIT-0': 'bg-green-100 text-green-700',
  'Apache-2.0': 'bg-blue-100 text-blue-700',
  'BSD-2-Clause': 'bg-purple-100 text-purple-700',
  'MIT OR GPL-3.0': 'bg-green-100 text-green-700',
  'MPL-2.0': 'bg-orange-100 text-orange-700',
};

export function ThirdPartyLicensesPage() {
  const navigate = useNavigate();
  const frontendLibs = libraries.filter(l => l.category === 'frontend');
  const backendLibs = libraries.filter(l => l.category === 'backend');

  return (
    <div className="max-w-4xl mx-auto py-8 px-4">
      <div className="mb-6 flex items-center gap-4">
        <button
          onClick={() => navigate(-1)}
          className="flex items-center gap-2 text-sm text-gray-500 hover:text-gray-800 transition-colors"
        >
          <ArrowLeftIcon className="h-4 w-4" />
          Zurück
        </button>
        <h1 className="text-xl font-semibold text-gray-900">Verwendete Drittanbieter-Bibliotheken</h1>
      </div>

      <p className="text-sm text-gray-500 mb-8">
        Diese Anwendung verwendet die folgenden Open-Source-Bibliotheken. Wir danken den jeweiligen
        Autoren und der Community für ihre Arbeit.
      </p>

      <Section title="Frontend" libs={frontendLibs} />
      <Section title="Backend" libs={backendLibs} />
    </div>
  );
}

function Section({ title, libs }: { title: string; libs: Library[] }) {
  return (
    <div className="mb-10">
      <h2 className="text-base font-semibold text-gray-700 mb-3 uppercase tracking-wide">{title}</h2>
      <div className="border border-gray-200 rounded-lg overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200 text-sm">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-4 py-3 text-left font-medium text-gray-500">Bibliothek</th>
              <th className="px-4 py-3 text-left font-medium text-gray-500">Version</th>
              <th className="px-4 py-3 text-left font-medium text-gray-500">Lizenz</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-100">
            {libs.map(lib => (
              <tr key={lib.name} className="hover:bg-gray-50">
                <td className="px-4 py-3">
                  <a
                    href={lib.url}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-red-600 hover:underline font-medium"
                  >
                    {lib.name}
                  </a>
                </td>
                <td className="px-4 py-3 text-gray-500 font-mono">{lib.version}</td>
                <td className="px-4 py-3">
                  <span className={`inline-block px-2 py-0.5 rounded-sm text-xs font-medium ${licenseBadgeColor[lib.license] ?? 'bg-gray-100 text-gray-600'}`}>
                    {lib.license}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
