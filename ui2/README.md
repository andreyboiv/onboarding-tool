# Onboarding Tool (Frontend)

Angular 18.2.1
<p>
`ng serve` - wird einen Entwicklungsserver ausgeführt
<p>
`http://localhost:4200/`- die Hauptseite des Tools
<p>
<p>
Das Backend Projekt: 
<p>
<a href="https://github.com/andreyboiv/onboarding-tool-backend">https://github.com/andreyboiv/onboarding-tool-backend</a>

<p>
<p>
<b><h2>Kurze Beschreibung des Projektes (fachlich):</h2></b>
<p>
Ein neuer Mitarbeiter hat eine gewisse 
Anzahl von Onboarding Aufgaben zu erledigen. 
Wenn alle diese Aufgaben vom Mitarbeiter erledigt sind, 
soll der Workflow ja beenden. 
Dabei wird der Mitarbeiter vom System ausgeloggt 
bzw. deaktiviert und per E-Mail benachrichtigt. 
Die Benachrichtigung 
erhält das Onboarding Team entsprechend auch.
<p>
<p>

# Autorisierung
Die Autorisierung Form (siehe Screenshot unten). Man loggt sich da ja ein 
(wenn man früher natürlich schon registriert hat).
<p>
Dabei hat man die Möglichkeit bei der Form entweder sich zu registrieren 
 oder das Passwort zurücksetzten lassen 
(falls das Passwort z.B. verloren/vergessen wurde). Dafür gibt's ja die Links auf die neuen Forms 
("Zur Registrierung Form" und "Passwort zurücksetzen" entsprechend)

<p>

![img.png](src/images/autorisierung_form.png)

<p>

Es gibt eine Validierung bei der Autorisierung Form (siehe Screenshot unten) 
<p>

![img_1.png](src/images/autorisierung_form_validierung.png)

<p>

# Onboarding Haupt Form
So sieht die Onboarding HauptForm aus (siehe Screenshots). 
Auf die Links Spalte sind Kategorien. 
Auf die rechts Spalte sind die Tasks, die zu entsprechenden Kategorien von Tasks gehören.
<p>


![img.png](src/images/hauptmenu_onboarding_tool.png)

<p>
So sieht die Mobile Sicht der Onboarding HauptForm aus:
<p>

![img.png](src/images/hauptmenu_onboarding_tool_mobile_sicht.png)

<p>
Wenn mann alle Tasks erledigt markiert, 
kommt die Meldung (siehe Screenshot unten). 
<p>

![img_19.png](src/images/hauptmenu_onboarding_tool_meldung_alle_tasks_durch.png)

<p>
Wenn man "Daten übermitteln" klickt, 
kommt die Meldung:
<p>

![img.png](src/images/meldung_account_deaktiviert.png)

<p>
Dabei kommt eine E-Mail:

<p>

![img_1.png](src/images/email_account_deaktiviert.png) 

<p>

# Registrierung
So sieht die Registrierung Form aus (siehe Screenshot unten). 
<p>
Da gibt man seinen Login (Namen), 
E-Mail, Passwort und ein Captcha für eine extra Validierung ein. Dabei
gibt's die Möglichkeit, auf die Autorisierung Form zu gehen.
<p>

![img_2.png](src/images/registrierung_form.png)

<p>

Es gibt eine Validierung bei der Registrierung Form (siehe Screenshot unten)
<p>

![img_3.png](src/images/registrierung_form_validierung.png)

<p>

Wenn man die Registrierung Form mit gültigen Daten ausfüllt, und man den "Registrieren" Button klickt, 
kann man die Meldung sehen (siehe Screenshot)

![img_4.png](src/images/meldung_erfolgreiche_registrierung.png)

<p>
Dabei soll eine E-Mail kommen:
<p>

![img_5.png](src/images/email_erfolgreiche_registrierung.png) 

<p>
Nachdem den Link geklickt wurde, kommt die Meldung:

<p>

![img_6.png](src/images/meldung_erfolgreiche_aktivierung.png)

<p>

Dabei kommt wieder eine E-Mail, aber schon mit Begrüßung:
<p>

![img_7.png](src/images/email_erfolgreiche_aktivierung.png)

<p>
Wenn man auf die Idee kommt, den gleichen Link wieder aufzurufen, kommt dann die Meldung (siehe Screenshot unten). Gleiche Meldung kommt wenn der Link ungültig wäre (bzw. UUID ungültig wäre)
<p>

![img_8.png](src/images/fehlermeldung_aktivierung.png)

<p>

# Zurücksetzen vom Passwort

So sieht die Form vom Zurücksetzen von seinem Passwort:
<p>

![img_9.png](src/images/passwort_zuruecksetzen_form.png)

<p>
Dabei kann man nur die E-Mail im Feld eingeben, die tatsächlich im System registriert ist. In diesem Fall kommt die Meldung:
<p>

![img_10.png](src/images/passwort_zuruecksetzen_meldung_email_existiert_nicht.png)

<p>
Wenn man eine E-Mail beim Password Zurücksetzen eingibt, die gültig ist (existiert im System), soll die Meldung kommen:
<p>

![img_11.png](src/images/meldung_anweisungen_passwort_zuruecksetzen.png)

<p>

Dabei soll ja eine E-Mail kommen:
<p>

![img_12.png](src/images/email_passwort_zuruecksetzen.png)

<p>

Den Link in der E-Mail geht auf die Seite, wo man sein Passwort ändern kann:
<p>

![img_13.png](src/images/passwort_aendern_form.png)

<p>

Wenn 5 min vorbei ist, ist der Link nicht mehr aufrufbar (siehe Screenshot unten). 
Gleiche Meldung kommt, wenn der Link nicht gültig ist:
<p>

![img_14.png](src/images/fehlermeldung_passwort_aenderung.png)

<p>

Wenn das Passwort geändert wurde, kommt die Meldung:

<p>

![img_15.png](src/images/meldung_passwort_geandert.png)

<p>
