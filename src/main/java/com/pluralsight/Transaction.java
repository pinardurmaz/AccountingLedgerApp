package com.pluralsight;
// her islemi temsil eden sinif
public class Transaction {
    private String date;
    private String time;
    private String description; //aciklama
    private String vendor; //satici veya ilgili kisi kurum
    private double amount; //tutar(+gelir - gider)

    // constructor ;islem olustururken kullanilir
    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // cvs formatinda yazmak icin kulllanilan metod
    public String toCSV() {
        return date + "|" + time + "|" + description + "|" + vendor + "|" + amount;
    }

    // Getter metodlari -diger siniflar bu verileri okuyabilir
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getDescription() { return description; }
    public String getVendor() { return vendor; }
    public double getAmount() { return amount; }
}