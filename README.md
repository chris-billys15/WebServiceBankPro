# Web Service Bank 

Web service Bank adalah sebuah endpoint yang memiliki fungsi untuk : <br>
1. Memvalidasi Rekening <br>
2. Membuat Akun Virtual <br>
3. Mentransfer Uang ke suatu akun <br>
4. Mengecek history akun <br>
5. Mengecek apakah ada sebuah transaksi dalam rentang waktu tertentu <br>


# END POINT 

http://100.26.43.243:8080/bankprowebservice-1.0-SNAPSHOT/NewWebService?wsdl

#Basis Data yang Digunakan

Basis data yang digunakan menggunakan mesin mysql dengan nama database bankprodb. <br>
Database tersebut memiliki tabel seperti berikut :
1. Tabel Nasabah : (nomornasabah int, namapengguna varchar(50), namabank varchar(50), saldo int )
2. Tabel virtualaccount : (IDVirtualAccount int ,NomorNasabah int, virtualaccount int, nomorpengirim int)
3. Tabel transaksi : (ID_Transaksi int, NomorNasabah int, waktu datetime, jenis varchar, jumlah int, rekeningterkait int)


#Pembagian Tugas DPPL
1. CI/CD : 1317062
2. Linting : 13517050
3. Eksplorasi Virtual Machine : 13517047

