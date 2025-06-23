package com.example.pengingattugas;

public class Tugas {
    private int id;
    private String namaTugas;
    private String mataKuliah;
    private long deadlineTimestamp;

    public Tugas(int id, String namaTugas, String mataKuliah, long deadlineTimestamp) {
        this.id = id;
        this.namaTugas = namaTugas;
        this.mataKuliah = mataKuliah;
        this.deadlineTimestamp = deadlineTimestamp;
    }

    public int getId() {
        return id;
    }

    public String getNamaTugas() {
        return namaTugas;
    }

    public String getMataKuliah() {
        return mataKuliah;
    }

    public long getDeadlineTimestamp() {
        return deadlineTimestamp;
    }
}