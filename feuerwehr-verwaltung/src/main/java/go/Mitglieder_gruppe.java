/*
 * Decompiled with CFR 0.152.
 */
package go;

public class Mitglieder_gruppe {
    private int id;
    private int personalnummer;
    private int nextPersonalnummer;
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonalnummer() {
        return this.personalnummer;
    }

    public void setPersonalnummer(int personalnummer) {
        this.personalnummer = personalnummer;
    }

    public int getNextPersonalnummer() {
        return this.nextPersonalnummer;
    }

    public void setNextPersonalnummer(int nextPersonalnummer) {
        this.nextPersonalnummer = nextPersonalnummer;
    }
}

