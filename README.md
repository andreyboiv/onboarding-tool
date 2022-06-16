# Team Tasks Planning Projekt

Spring Boot Projekt

Spring Boot Version: 2.7.0
<p>
Java Version: 11
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
7. PostgreSQL Driver
8. JWT (JSON Web Token) Library
9. Lombok
10. Maven
  
<b>Frontend Components (Dependencies):</b>
<p>
Frontend ist noch nicht realisiert und befindet sich in der Entwicklungsphase
<p>
<p>
<p>
<b><h2>Allgemeine Beschreibung des Projektes:</h2></b>
<p>
<ins>Die Hauptfunktionalität besteht darin, dass die Mitarbeiter eines Unternehmens ihre individuellen Arbeitsaufgaben (Tasks) verwalten und planen können.</ins>
Die Aufgaben der Mitarbeiter haben ihre eigenen Kategorien der Komplexität, Prioritäten und können von Mitarbeitern entweder erledigt oder nicht erledigt werden. 
Für alle Aufgaben von Mitarbeitern kann man die allgemeine Statistik von abgeschlossenen und nicht abgeschlossenen Aufgaben betrachten und damit sowohl die Arbeit jedes einzelnen Mitarbeiters als auch die Arbeit des gesamten Teams als Ganzes auswerten. Mitarbeiter unterscheiden sich in Bezug auf Befugnisse. Ein Mitarbeiter kann viele verschiedene Befugnisse haben. 
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
1. Die Tabelle <b><ins>"employee"</ins></b> ist eine Tabelle zum Speichern von Mitarbeiterdaten (Credentials Daten). Die Daten in der Tabelle werden manuell von Mitarbeitern/Administrator ausgefüllt. Die Tabelledaten dienen sowohl für Präsentation als auch für Modifikation.
2. Die Tabelle <b><ins>"stat"</ins></b> zeigt Statistik von allen abgeschlossenen und fehlgeschlagenen Aufgaben (Tasks) für jeden Mitarbeiter.
3. Die Tabelle <b><ins>"activity"</ins></b> zeigt die Registrierungsdaten eines Mitarbeiters an. Die Tabellen "stat" und "activity" steuern Mitarbeiterdaten wie die Tabelle "employee" und stehen zur Tabelle "employee" in einer 1:1-Beziehung. Die Daten der Tabellen "stat" und "activity" sind von einem SQL Trigger gesteuert. Die Tabellendaten ("stat" und "activity") dienen nur für Präsentation und nicht für Modifikation.
4. Die Tabelle <b><ins>"powers"</ins></b> ist eine Tabelle, die Befugnisse eines Mitarbeiters enthält. Standardmäßig hat die Tabelle die folgenden Berechtigungen: 
  a. Berechtigungen eines Mitarbeiters (USER) 
  b. Berechtigungen eines Administrators (ADMIN). 
Die Daten in der Tabelle dienen nur für Präsentation und nicht für Änderung.
5. Die Tabelle <b><ins>"employee_powers"</ins></b> ist eine Tabelle eines Mitarbeiters und seiner Befugnisse. Die Befugnissdaten werden aus der Tabelle "powers" entnommen. Dementsprechend sind die Tabellen "employee" und "powers" mit einer N-zu-N-Beziehung durch die Tabelle "employee_powers" verbunden. Ein Mitarbeiter kann mehrere Befugnisse haben. Die Daten der Tabelle "employee_powers" werden anhand eines SQL Triggers gefüllt, wenn ein neuer Mitarbeiter im System registriert wird. Bei der Registrierung eines neuen Mitarbeiters im System werden diesem Mitarbeiter standardmäßig nur die Rechte eines Mitarbeiters (USER) zugewiesen. Die Daten in der Tabelle „employee_powers“ dienen nur für Präsentation und nicht für Änderung. Gleichzeitig dürfen einige Mitarbeiter ausnahmsweise Administratorrechte haben. Dazu muss der Datenbank Administrator einen entsprechenden Eintrag in die Tabelle "employee_powers" einfügen.
6. Tabelle <b><ins>"category"</ins></b> ist eine Tabelle, die Aufgabenkategorien enthält. Die Standarddaten der Tabelle "category" werden anhand eines SQL Triggers gefüllt, wenn ein neuer Mitarbeiter im System registriert wird. Die Daten der Tabelle werden hauptsächlich nur für Präsentation verwendet. Dies bedeutet natürlich nicht, dass keine neue Datensätze in die Tabelle "category" hinzugefügt werden können. Man muss aber dabei auf statistische Daten achten (sie werden anhand eines SQL Triggers gesteuert), dass sie nicht geändert werden. In diesem Fall gibt es einige Begrenzungen in Backend (Z.b "insertable = false" und "updatable = false" Parameter)
7. Tabelle <b><ins>"priority"</ins></b> ist eine Tabelle, die Aufgabenprioritäten enthält. Die Standarddaten der Tabelle "priority" werden anhand eines SQL Triggers gefüllt, wenn ein neuer Mitarbeiter im System registriert wird. Die Daten in der Tabelle können später ergänzt und geändert werden.
8. Tabelle <b><ins>"task"</ins></b> ist eine Tabelle, die Aufgaben eines Mitarbeiters enthält. Aufgaben haben Beschreibung, Kategorien und Prioritäten. Eine Aufgabe ist zu einem Mitarbeiter zugeordnet. Die Daten der Tabelle "task" werden anhand eines SQL Triggers ausgefüllt, wenn ein neuer Mitarbeiter im System registriert wird. Die Daten (außer Statistik) der Tabelle können später ergänzt und geändert werden.

<b><h2>Beschreibung des Projektes (Backend):</h2></b>

  1. Eine Datenbank mit Geschäftslogik (Triggers) wurde entworfen
  2. Erstellen und Konfigurieren eines Spring Boot-Projekts
  3. Datenbank-Mapping wurde realisiert (entsprechende Entity-Klassen wurden erstellt). 
  Dabei wurden Beziehungen (One-to-One, One-to-Many, Many-to-One, Many-to-Many) 
  mit entsprechenden Annotationen aufgebaut.
  4. SSL-Authentifizierung wurde implementiert (lokal)
  5. Entsprechde Repositories, Services und Controller wurden implementiert
  6. AOP für Controllers Methods  wurde implementiert
  7. Die Mitarbeiter-Authentifizierung wurde mit JWT-Token implementiert
  8. Die Mitarbeiter-Autorisierung wurde eingestellt
  
  Das sind die Enttry-Points für das Projekt (Restful Services) :
  <p>
    
  ![alt text](https://boivalenko.com/img/java_ep/spring/projekt_2/postman.jpg?raw=true)
    
  Das Screenshot wurde in Postman gemacht. Dabei ist es wichtig, dass Z.b. Mitarbeiter mit USER Rechte beschränkten Zugriff auf Services haben. 
  Das kann in Backend z.B.mit dieser Annotatotion: "@PreAuthorize("hasAuthority('USER')")" einstellen. 
  Damit werden Mitarbeiter nach dieser Authority validiert.
  <p>
  <p>
  <p>
<b><h2>TODO zum Projekt:</h2></b>
    
    1. Frontend (Angular) realisieren
    2. Frontend mit Backend verknüpfen
    2. Das ganzes Projekt (Backend und Frontend) auf Heroku deployen
