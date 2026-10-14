import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useLocalSearchParams } from 'expo-router';
import { useState } from 'react';
import { KeyboardAvoidingView, Platform, ScrollView, StyleSheet } from 'react-native';

import { membersApi, operationsApi } from '@/api/client';
import type { MemberOption, PersonnelSection } from '@/api/types';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { ChoiceChips } from '@/components/ui/choice-chips';
import { QueryState } from '@/components/ui/query-state';
import { ListRow, Section } from '@/components/ui/section';
import { TextField } from '@/components/ui/text-field';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { functionLabel, memberName, PERSONNEL_FUNCTIONS } from '@/lib/labels';
import { goBackOr } from '@/lib/navigation';

const NO_VEHICLE = '';

/**
 * Adds members to the Kräftenachweis one after another: the screen stays open after each one, so
 * a whole crew can be entered in a row.
 */
export default function AddPersonnelScreen() {
  const params = useLocalSearchParams<{ id: string; vehicleName?: string; section?: string }>();
  const id = Number(params.id);
  const queryClient = useQueryClient();

  const operation = useQuery({ queryKey: ['operations', id], queryFn: () => operationsApi.get(id) });
  const personnel = useQuery({ queryKey: ['operations', id, 'personnel'], queryFn: () => operationsApi.getPersonnel(id) });
  const members = useQuery({ queryKey: ['members', 'options'], queryFn: membersApi.listOptions });

  const [section, setSection] = useState<PersonnelSection>(params.section === 'reinforcement' ? 'reinforcement' : 'deployed');
  // null = untouched: deployed crew goes to the first vehicle, reinforcement has none
  const [chosenVehicle, setVehicleName] = useState<string | null>(params.vehicleName ?? null);
  const [fn, setFn] = useState<string>(PERSONNEL_FUNCTIONS[0]);
  const [search, setSearch] = useState('');
  const [lastAdded, setLastAdded] = useState<string | null>(null);

  const vehicleNames = [...new Set((operation.data?.times ?? []).map((t) => t.vehicleName.trim()).filter(Boolean))];
  const vehicleName = chosenVehicle ?? (section === 'deployed' ? (vehicleNames[0] ?? NO_VEHICLE) : NO_VEHICLE);
  const added = new Set((personnel.data ?? []).map((p) => p.memberId));
  const term = search.trim().toLowerCase();
  const candidates = (members.data ?? []).filter(
    (m) => !added.has(m.id) && (!term || memberName(m).toLowerCase().includes(term)),
  );

  const add = useMutation({
    mutationFn: (member: MemberOption) =>
      operationsApi.addPersonnel(id, { memberId: member.id, vehicleName, function: fn, section }),
    onSuccess: (_result, member) => {
      queryClient.invalidateQueries({ queryKey: ['operations', id, 'personnel'] });
      setLastAdded(`${memberName(member)} als ${functionLabel(fn)} hinzugefügt.`);
      setSearch('');
    },
  });

  return (
    <ThemedView style={styles.container}>
      <KeyboardAvoidingView style={styles.container} behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
        <ScrollView contentContainerStyle={styles.content} keyboardShouldPersistTaps="handled">
          <ChoiceChips<PersonnelSection>
            label="Bereich"
            choices={[
              { value: 'deployed', label: 'Eingesetzt' },
              { value: 'reinforcement', label: 'Nachgerückt' },
            ]}
            value={section}
            onChange={setSection}
          />
          <ChoiceChips
            label="Fahrzeug"
            choices={[...vehicleNames.map((name) => ({ value: name, label: name })), { value: NO_VEHICLE, label: 'ohne Fahrzeug' }]}
            value={vehicleName}
            onChange={setVehicleName}
          />
          <ChoiceChips
            label="Funktion"
            choices={PERSONNEL_FUNCTIONS.map((value) => ({ value, label: functionLabel(value) }))}
            value={fn}
            onChange={setFn}
          />

          {lastAdded && (
            <ThemedText type="small" themeColor="success">
              {lastAdded}
            </ThemedText>
          )}
          {add.error && (
            <ThemedText type="small" themeColor="danger">
              {add.error instanceof TypeError ? 'Server nicht erreichbar' : add.error.message}
            </ThemedText>
          )}

          <TextField label="Mitglied suchen" value={search} onChangeText={setSearch} placeholder="Name" autoCorrect={false} />
          <QueryState isPending={members.isPending} error={members.error} />
          {members.data && (
            <Section title="Zum Hinzufügen antippen" empty={term ? 'Keine Treffer' : 'Alle Mitglieder sind eingetragen'}>
              {candidates.map((member) => (
                <ListRow
                  key={member.id}
                  title={memberName(member)}
                  subtitle={member.rank ?? undefined}
                  detail="+"
                  onPress={() => !add.isPending && add.mutate(member)}
                />
              ))}
            </Section>
          )}
          <Button
            title="Fertig"
            onPress={() => goBackOr({ pathname: '/operation/[id]', params: { id: String(id) } })}
          />
        </ScrollView>
      </KeyboardAvoidingView>
    </ThemedView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  content: {
    padding: Spacing.three,
    gap: Spacing.three,
    width: '100%',
    maxWidth: MaxContentWidth,
    alignSelf: 'center',
  },
});
