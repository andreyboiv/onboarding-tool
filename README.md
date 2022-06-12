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

<b>Allgemeine Beschreibung des Projektes:</b>
<p>
<ins>Die Hauptfunktionalität besteht darin, dass die Mitarbeiter eines Unternehmens ihre individuellen Arbeitsaufgaben (Tasks) verwalten und planen können.</ins>
Die Aufgaben der Mitarbeiter haben ihre eigenen Kategorien der Komplexität, Prioritäten und können von Mitarbeitern entweder erledigt oder nicht erledigt werden. 
Für alle Aufgaben von Mitarbeitern kann man die allgemeine Statistik erledigter und unerfüllter Aufgaben einsehen und damit sowohl die Arbeit jedes einzelnen Mitarbeiters als auch die Arbeit des gesamten Teams als Ganzes auswerten. Mitarbeiter unterscheiden sich in Bezug auf Befugnisse. Ein Mitarbeiter kann viele verschiedene Befugnisse haben. 
Es gibt einen Registrierungsbestätigungsprozess und einen Mitarbeiterautorisierungsprozess im System.
<p>
  
<b>Allgemeine Beschreibung des Projektes finden Sie auch in meinem Video:</b>
<p>
TODO

<b>Beschreibung des Projektes (aus Datenbank Sicht):</b>

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

<b>Beschreibung der Tabellen:</b>
1. Die Tabelle <ins>"employee"</ins> ist eine Tabelle zum Speichern von Mitarbeiterdaten (Credentials Daten). Die Daten in der Tabelle werden manuell von Mitarbeitern/Administrator ausgefüllt. Die Tabelledaten dienen sowohl für Präsentation als auch für Modifikation.
2. Die Tabelle <ins>"stat"</ins> zeigt Statistik aller abgeschlossenen und fehlgeschlagenen Aufgaben (Tasks) für jeden Mitarbeiter.
3. Die Tabelle <ins>"activity"</ins> zeigt die Registrierungsdaten eines Arbeiters an. Die Tabellen "stat" und "activity" steuern Mitarbeiterdaten wie die Tabelle "employee" und stehen zur Tabelle "employee" in einer 1:1-Beziehung. Die Daten der Tabellen "stat" und "activity" sind von einem SQL Trigger gesteuert. Die Tabellendaten ("stat" und "activity") dienen nur für Präsentation und nicht für Änderung.
4. Die Tabelle <ins>"powers"</ins> ist eine Tabelle, die Befugnisse eines Mitarbeiters beschreibt. Standardmäßig sind der Datenbank bereits Berechtigungen hinzugefügt: 
  a. Berechtigungen eines Mitarbeiters (User) 
  b. Berechtigungen eines Administrators (Admin). Die Daten in der Tabelle dienen nur für Präsentation und nicht für Änderung.
5. Die Tabelle <ins>"employee_powers"</ins> ist eine Tabelle eines Mitarbeiters und seiner Befugnisse. Die Befugnissdaten werden aus der Tabelle "powers" genommen. Dementsprechend sind die Tabellen "employee" und "powers" durch eine N-zu-N-Beziehung durch die Tabelle "employee_powers" verbunden. Ein Mitarbeiter kann viele Befugnisse haben. Die Daten der Tabelle "employee_powers" werden über einen SQL Trigger gefüllt, wenn ein neuer Mitarbeiter im System registriert wird. Bei der Registrierung eines neuen Mitarbeiters im System werden diesem Mitarbeiter standardmäßig nur die Rechte eines Mitarbeiters (User) zugewiesen. Die Daten in der Tabelle „employee_powers“ dienen nur für Präsentation und nicht für Änderung. Gleichzeitig dürfen einige Mitarbeiter ausnahmsweise Administratorrechte erhalten. Dazu muss der DB Admin einen entsprechenden Eintrag in die Tabelle "employee_powers" machen.
6. Tabelle <ins>"category"</ins> ist eine Tabelle, die Aufgabenkategorien enthält. Die Standarddaten der Tabelle "category" werden anhand eines SQL Trigger gefüllt, wenn ein neuer Mitarbeiter im System registriert wird. Die Daten der Tabelle werden hauptsächlich nur für Präsentation verwendet. Dies bedeutet natürlich nicht, dass keine neue Datensätze in die Tabelle "category" hinzugefügt werden können. Man muss aber dabei auf statistische Daten achten (sie werden durch einen SQL Trigger gesteuert), dass sie nicht geändert werden. In diesem Fall gibt es einige Begrenzungen in Backend (Z.b "insertable = false" und "updatable = false" Parameter)
7. Tabelle <ins>"priority"</ins> ist eine Tabelle, die Aufgabenprioritäten enthält. Die Standarddaten der Tabelle "priority" werden über anhand eines SQL Trigger gefüllt, wenn ein neuer Arbeiter im System registriert wird. Die Daten in der Tabelle können später ergänzt und geändert werden.
8. Tabelle <ins>"task"</ins> ist eine Tabelle, die Aufgaben eines Mitarbeiters enthält. Aufgaben haben eine Beschreibung, Kategorien und Prioritäten. Eine Aufgabe ist zu einem Mitarbeiter zugeordnet. Die Daten der Tabelle "task" werden anhand eines SQL Trigger ausgefüllt, wenn ein neuer Mitarbeiter im System registriert wird. Die Daten (außer Statistik) der Tabelle können später ergänzt und geändert werden.

<b>Beschreibung des Projektes (aus Datenbank Sicht) finden Sie auch in meinem Video:</b>
<p>
TODO
<p>

<b>Beschreibung des Projektes (Backend) finden Sie in meinem Video:</b>
<p>
TODO
<p>

<b>Beschreibung des Projektes (Frontend) finden Sie in meinem Video:</b>
<p>
TODO
<p>
