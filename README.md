# Team Tasks Planning Projekt

Spring Boot Projekt

Spring Boot Version: 3.2.0
<p>
Java Version: 16
<p>
Datenbank: PostgreSQL
<p>

<b>Backend Components (Dependencies):</b>
1. Spring Boot DevTools
2. Spring Web
3. Spring Data JPA
4. Spring Security
5. Spring AOP
6. Spring Validation
7. Spring Mail
8. Spring Test
9. Maven
10. PostgreSQL Driver. Version 42.6.0
11. JWT (JSON Web Token) Library. Version 0.12.3 
12. Lombok. Version 1.18.30
  
<b>Frontend Components (Dependencies):</b>
<p>
Frontend ist noch nicht realisiert und befindet sich in der Entwicklungsphase
<p>
<p>
<p><b><h2>Beschreibung des Projektes (Backend Controller Entry-Points Sicht. Siehe AuthController Klasse):</h2></b>
<p>

1. <b>Registrierung. (auth/register)</b>

Es wird einen neuen Employee registriert. Das Password wird mit
BCryptPasswordEncoder (BCrypt - https://ru.bitcoinwiki.org/wiki/Bcrypt) kodiert und im kodierten Form in der DB abgespeichert. 
Nach der erfolgreichen Registrierung wird ein Link 
zum Aktivieren des Accounts 
von einem Employee an die 
angegebene E-Mail (bei der Registrierung) abgeschickt.

2.  <b>Aktivierung. (auth/activate-account)</b>

Es wird einen neuen Employee aktiviert. Nach dem erfolgreichen 
Aktivieren erhält der Employee eine E-Mail mit Begrüßung.
<p>

3. <b>LogIn. (auth/login)</b>

Ein Employee kann sich nach seinem Account Aktivieren anmelden bzw. 
einloggen im System. 
Damit wird ein gültiges JWT-Token auf dem Server erstellt. 
Das JWT-Token wird vom 1 Tag Dauer gültig. 
Es wird keine relevante Information in dem JWT-Token abgespeichert 
(außer Login und Role eines Employees). 
Das Aktivieren darf 
nur ein mal durchgeführt werden. 
Wenn der Link (zum Aktivieren) warum auch immer verloren geht, kann man 
anhand <b>"auth/resend-activate-email"</b> 
eine neue E-Mail für die Aktivierung anfordern. 
Da muss man dafür entweder ein Login oder E-Mail eingeben.
<p>

4. <b>LogOut. (auth/logout)</b>

<ins>Der Request Ersteller muss angemeldet sein und dabei die Role "User" oder "Admin" haben.</ins>

Ein Employee kann sich abmelden bzw. 
ausloggen aus dem System.
Damit wird zunächst eine JWT Validierung von dem existierenden JWT-Token
durchgeführt (siehe AuthTokenFilter) 
und dann wird ein Cookie bzw. JWT-Token 
(das in dem Cookie abgespeichert wird) erstellt. Der neue Token 
wird dann vom Browser automatisch entfernt/gelöscht, 
weil da in diesem neuen JWT-Token eine Parameter "maxAge(0)"
eingesetzt wird.
<p>

5. <b>Password Ändern. (auth/update-password)</b>

<ins>Der Request Ersteller muss angemeldet sein und dabei die Role "User" haben.</ins>

Dabei wird es zunächst eine JWT Validierung von dem existierenden JWT-Token
durchgeführt (siehe AuthTokenFilter)

Bei einem Employee kann das Password geändert werden.
Vor allem braucht man aber für eine Password Änderung ein Bearer Token, 
das anhand <b>"auth/send-reset-password-email"</b> zu erhalten ist.
Nach der erfolgreichen Password Änderung erhält der Employee eine
Benachrichtigung (eine E-Mail), dass sein Password geändert wurde.
<p>

6. <b>Account Deaktivieren. (auth/de-activate-account)</b>

Bei einem Employee kann das Account deaktiviert werden.

<ins>Der Request Ersteller muss dabei die Role "Admin" haben.</ins>

Dabei wird es zunächst eine JWT Validierung von dem existierenden JWT-Token
durchgeführt (siehe AuthTokenFilter).
Nach dem erfolgreichen Account Deaktivieren erhält der Employee eine
Benachrichtigung (eine E-Mail), dass sein Account deaktiviert wurde.
<p>
<p>
<p>

<b>
Siehe Screenshot von Postman.
</b>

![alt text](https://boivalenko.com/img/java_ep/spring/projekt_2/postman.jpg?raw=true)

Der Screenshot zeigt, welche Backend Controller da sind. Die entsprechende Requests 
für Postman kann man unter "\resources\postman_tests_restful_webservices_requests" im Projekt finden.

<p>
<p>
<b><h2>Allgemeine Beschreibung des Projektes:</h2></b>
<p>
<ins>Die Hauptfunktionalität besteht darin, dass die Mitarbeiter eines Unternehmens ihre individuellen Arbeitsaufgaben (Tasks) verwalten und planen können.</ins>
Die Aufgaben der Mitarbeiter haben ihre eigenen Kategorien der Komplexität, Prioritäten und können von Mitarbeitern entweder erledigt oder nicht erledigt werden. 
Für alle Aufgaben von Mitarbeitern kann man die allgemeine Statistik von abgeschlossenen und nicht abgeschlossenen Aufgaben betrachten und damit sowohl die Arbeit 
jedes einzelnen Mitarbeiters als auch die Arbeit des gesamten Teams als Ganzes auswerten. Mitarbeiter unterscheiden sich in Bezug auf Befugnisse. Ein Mitarbeiter kann viele verschiedene Befugnisse haben. 
Im System gibt es einen Registrierungsbestätigungsprozess und einen Mitarbeiterautorisierungsprozess.
<p>
<p>
  
<b><h2>Beschreibung des Projektes (aus Datenbank Sicht):</h2></b>

![alt text](https://boivalenko.com/img/java_ep/spring/projekt_2/db_sicht.jpg?raw=true)

<b>Die Datenbank (DB) enthält 8 Tabellen und alle Tabellen haben Primary Key mit Auto_Increment. Das sind die Tabellen:</b>
  
1. employee
2. stat
3. activity
4. powers
5. employee_powers
6. category
7. priority
8. task

<b><h3>Beschreibung der Tabellen:</h3></b>
1. Die Tabelle <b><ins>"employee"</ins></b> ist eine Tabelle zum Speichern von Mitarbeiterdaten (Credentials Daten). 
Die Daten in der Tabelle werden manuell von Mitarbeitern/Administrator ausgefüllt. 
Die Tabelledaten dienen sowohl für Präsentation als auch für Modifikation.
<p>
2. Die Tabelle <b><ins>"stat"</ins></b> zeigt Statistik von allen abgeschlossenen und fehlgeschlagenen Aufgaben (Tasks) für jeden Mitarbeiter.
<p>
3. Die Tabelle <b><ins>"activity"</ins></b> zeigt die Registrierungsdaten eines Mitarbeiters an. Die Tabellen "stat" und "activity" steuern Mitarbeiterdaten wie die Tabelle "employee" und stehen zur Tabelle "employee" in einer 1:1-Beziehung. Die Daten der Tabellen "stat" und "activity" sind von einem SQL Trigger gesteuert. Die Tabellendaten ("stat" und "activity") dienen nur für Präsentation und nicht für Modifikation.
<p>
4. Die Tabelle <b><ins>"powers"</ins></b> ist eine Tabelle, die Befugnisse eines Mitarbeiters enthält. Standardmäßig hat die Tabelle die folgenden Berechtigungen:
<br>
   &nbsp;&nbsp;&nbsp;  a. Berechtigungen eines Mitarbeiters (USER) 
<br>
   &nbsp;&nbsp;&nbsp;  b. Berechtigungen eines Administrators (ADMIN).
<br>
Die Daten in der Tabelle dienen nur für Präsentation und nicht für Änderung.
<p>
5. Die Tabelle <b><ins>"employee_powers"</ins></b> ist eine Tabelle eines Mitarbeiters und seiner Befugnisse. Die Befugnissdaten werden aus der Tabelle "powers" entnommen. Dementsprechend sind die Tabellen "employee" und "powers" mit einer N-zu-N-Beziehung durch die Tabelle "employee_powers" verbunden. Ein Mitarbeiter kann mehrere Befugnisse haben. Die Daten der Tabelle "employee_powers" werden anhand eines SQL Triggers gefüllt, wenn ein neuer Mitarbeiter im System registriert wird. Bei der Registrierung eines neuen Mitarbeiters im System werden diesem Mitarbeiter standardmäßig nur die Rechte eines Mitarbeiters (USER) zugewiesen. Die Daten in der Tabelle „employee_powers“ dienen nur für Präsentation und nicht für Änderung. Gleichzeitig dürfen einige Mitarbeiter ausnahmsweise Administratorrechte haben. Dazu muss der Datenbank Administrator einen entsprechenden Eintrag in die Tabelle "employee_powers" einfügen.
<p>
6. Tabelle <b><ins>"category"</ins></b> ist eine Tabelle, die Aufgabenkategorien enthält. Die Standarddaten der Tabelle "category" werden anhand eines SQL Triggers gefüllt, wenn ein neuer Mitarbeiter im System registriert wird. Die Daten der Tabelle werden hauptsächlich nur für Präsentation verwendet. Dies bedeutet natürlich nicht, dass keine neue Datensätze in die Tabelle "category" hinzugefügt werden können. Man muss aber dabei auf statistische Daten achten (sie werden anhand eines SQL Triggers gesteuert), dass sie nicht geändert werden. In diesem Fall gibt es einige Begrenzungen in Backend (Z.b "insertable = false" und "updatable = false" Parameter)
<p>
7. Tabelle <b><ins>"priority"</ins></b> ist eine Tabelle, die Aufgabenprioritäten enthält. Die Standarddaten der Tabelle "priority" werden anhand eines SQL Triggers gefüllt, wenn ein neuer Mitarbeiter im System registriert wird. Die Daten in der Tabelle können später ergänzt und geändert werden.
<p>
8. Tabelle <b><ins>"task"</ins></b> ist eine Tabelle, die Aufgaben eines Mitarbeiters enthält. Aufgaben haben Beschreibung, Kategorien und Prioritäten. Eine Aufgabe ist zu einem Mitarbeiter zugeordnet. Die Daten der Tabelle "task" werden anhand eines SQL Triggers ausgefüllt, wenn ein neuer Mitarbeiter im System registriert wird. Die Daten (außer Statistik) der Tabelle können später ergänzt und geändert werden.

<b><h2>Kurze Beschreibung des Projektes (Backend):</h2></b>

1. Eine Datenbank mit Geschäftslogik (Triggers) wurde entworfen
2. Erstellen und Konfigurieren eines Spring Boot-Projekts
3. Datenbank-Mapping wurde realisiert (entsprechende Entity-Klassen wurden erstellt). 
Dabei wurden Beziehungen (One-to-One, One-to-Many und Many-to-Many) 
mit entsprechenden Annotationen aufgebaut.
4. SSL-Authentifizierung wurde implementiert (lokal)
5. Entsprechde Repositories, Services und Controller wurden implementiert
6. AOP für Controllers Methods wurde implementiert
7. Die Mitarbeiter-Authentifizierung wurde mit JWT-Token implementiert
8. Die Mitarbeiter-Autorisierung wurde eingestellt
9. Mail Services wurden erstellt.
10. JUnit Tests wurden an einigen Stellen erstellt*.
<p>
<p>
* - Alle Controllers, Services und Repositories wurden manuell grundsätzlich getestet. 
Deshalb war die JUnit Testing nur ja an wichtigen Stellen nötig. 
Außerdem sind z.B. CategoryController, PriorityController, 
StatController und TaskController momentan von außen nicht erreichbar geworden. 
Wichtig zu testen sind die Klassen, die mit der Authentifizierung (Anmeldedaten) 
und mit der Autorisierung (Rechten) zu tun sind.Das sind die entsprechenden JUnit Tests 
dafür: AuthControllerTest, EmployeeServiceTest und 
EmployeeRepositoryTest.
<p>
<p>
