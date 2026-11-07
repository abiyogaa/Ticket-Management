# Ticket Management

## Perubahan terbatas
- @Component supaya strategy dapat di-inject oleh Spring.
- mock unit test mengembalikan ticket hasil service, sehingga priority yang diperiksa bukan hasil fixture yang sudah diatur.
- cast null pada ResponseEntity agar dapat dikompilasi pada Spring 7.
- file pendukung yang tidak disediakan gist: entry point, repository JDBC, konfigurasi H2, schema dan pom Maven.

## Menjalankan
Isi environment variable TICKETING_DB_USER dan TICKETING_DB_PASSWORD.
Jalankan Java 17: java -jar bin/ticketing-2.1.0.jar / mvn spring-boot:run
Endpoint: POST http://localhost:8080/api/tickets
Contoh JSON:
{"customerName":"Customer Uji","email":"uji@example.com","category":"FEEDBACK","description":"fraud report"}

Build ulang: mvn test lalu mvn package dengan JDK 17 dan Maven yang tersedia.
Test menargetkan JUnit 5.11.4.

