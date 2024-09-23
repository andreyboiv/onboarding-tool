PGDMP  -    '                |           postgres    16.2    16.2 I               0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    5    postgres    DATABASE     |   CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'German_Germany.1252';
    DROP DATABASE postgres;
                postgres    false                       0    0    DATABASE postgres    COMMENT     N   COMMENT ON DATABASE postgres IS 'default administrative connection database';
                   postgres    false    4889                        2615    16406    teamtasksplanning    SCHEMA     !   CREATE SCHEMA onboarding;
    DROP SCHEMA onboarding;
                postgres    false            �            1255    16407 
   add_task()    FUNCTION     8  CREATE FUNCTION onboarding.add_task() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN

    /* wenn category nicht leer ist & status vom task completed ist */
    if (coalesce(NEW.category_id,0)>0 and NEW.completed=1) then
		update onboarding.category set completed_count=(coalesce(completed_count,0)+1) where id=NEW.category_id and employee_id=new.employee_id;
	end if;
	
	/* wenn category nicht leer ist & status vom task uncompleted ist */
    if (coalesce(NEW.category_id,0)>0 and coalesce(NEW.completed,0)=0) then
		update onboarding.category set uncompleted_count=(coalesce(uncompleted_count, 0)+1) where id=NEW.category_id and employee_id=new.employee_id;
	end if;

	  /* gesamte statistik in der tabelle "stat" verändern */
	if coalesce(NEW.completed, 0)=1 then
		update onboarding.stat set completed_total=(coalesce(completed_total,0)+1) where employee_id=new.employee_id;
	else
		update onboarding.stat set uncompleted_total=(coalesce(uncompleted_total,0)+1) where employee_id=new.employee_id;
    end if;
    
	RETURN NEW;
END$$;
 ,   DROP FUNCTION onboarding.add_task();
       teamtasksplanning          postgres    false    7            �            1255    16408 
   delete_task()    FUNCTION     K  CREATE FUNCTION onboarding.delete_task() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN

     /* wenn category nicht leer ist & status vom task completed ist */
    if (coalesce(old.category_id,0)>0 and coalesce(old.completed,0)=1) then
		update onboarding.category set completed_count=(coalesce(completed_count,0)-1) where id=old.category_id and employee_id=old.employee_id;
	end if;
    
	/* wenn category nicht leer ist & status vom task uncompleted ist */
    if (coalesce(old.category_id,0)>0 and coalesce(old.completed,0)=0) then
		update onboarding.category set uncompleted_count=(coalesce(uncompleted_count,0)-1) where id=old.category_id and employee_id=old.employee_id;
	end if;
	
	 /* gesamte statistik in der tabelle "stat" verändern */
	if coalesce(old.completed,0)=1 then
		update onboarding.stat set completed_total=(coalesce(completed_total,0)-1) where employee_id=old.employee_id;
	else
		update onboarding.stat set uncompleted_total=(coalesce(uncompleted_total,0)-1) where employee_id=old.employee_id;
    end if;
    
	RETURN OLD; 
END
$$;
 /   DROP FUNCTION onboarding.delete_task();
       teamtasksplanning          postgres    false    7            �            1255    16409    new_employee()    FUNCTION     �  CREATE FUNCTION onboarding.new_employee() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE
	
	/* Um die ID der eingefügten Testdaten zu speichern, damit sie beim Erstellen von Testaufgaben verwendet werden können */
	priorId1 INTEGER; 
	priorId2 INTEGER;
	priorId3 INTEGER;
    priorId4 INTEGER;
    priorId5 INTEGER;
	
	catId1 INTEGER;
	catId2 INTEGER;
	catId3 INTEGER;
    catId4 INTEGER;
    catId5 INTEGER;
    catId6 INTEGER;
	
	/* Datum für Testing */
	now Date=NOW();
    oneDay Date=NOW()+INTERVAL '1 day';
    threeDays Date=NOW()+INTERVAL '3 day';
    oneWeek Date=NOW()+INTERVAL '7 day';

	/* ID aus der Tabelle "power" */
	powerId INTEGER=1;

BEGIN

	 /* Beim Einfügen eines neuen employee wird eine neue Zeile in der Tabelle "activity" erstellt */
     /* Das wurde im Code gemacht*/
     /*insert into onboarding.activity (uuid,employee_id) values (gen_random_uuid(),new.id);*/

     /* Beim Einfügen eines neuen employee wird eine neue Zeile in der Tabelle "stat" erstellt - dies sind keine Testdaten, sondern obligatorisch (sonst funktioniert die allgemeine Statistik nicht) */
     insert into onboarding.stat (employee_id) values (new.id);

     /* Beim Einfügen eines neuen employee werden die entsprechenden Categorien für den neuen employee erstellt */
     insert into onboarding.category (title,employee_id) values ('Einarbeitung',new.id) RETURNING id into catId1; /* Die ID des eingefügten Datensatzes wird in den Variablen oben gespeichert */
     insert into onboarding.category (title,employee_id) values ('Teammeetings',new.id) RETURNING id into catId2;
     insert into onboarding.category (title,employee_id) values ('Haupttasks',new.id) RETURNING id into catId3;

     /* Beim Einfügen eines neuen employee werden Prioritäten erstellt */
     insert into onboarding.priority (title,color,employee_id) values ('Niedrig','#caffdd',new.id) RETURNING id into priorId1;
     insert into onboarding.priority (title,color,employee_id) values ('Medium','#b488e3',new.id) RETURNING id into priorId2;
     insert into onboarding.priority (title,color,employee_id) values ('Hoch','#f05f5f',new.id) RETURNING id into priorId3;
     insert into onboarding.priority (title,color,employee_id) values ('Sehr hoch','#ed3434',new.id) RETURNING id into priorId4;
     insert into onboarding.priority (title,color,employee_id) values ('Sehr Kritisch','#a10303',new.id) RETURNING id into priorId5;

     /* Beim Einfügen eines neuen employee werden Testaufgaben für den erstellten employee erstellt */
     insert into onboarding.task (title,priority_id,category_id,task_date,employee_id) values ('Einen Arbeitsplatz mit einem Rechner bekommen',priorId4,catId3,now,new.id);
     insert into onboarding.task (title,priority_id,category_id,task_date,employee_id) values ('An dem Begrüßung/Kennenlernen Teammeeting der ganzen Firma teilnehmen',priorId4,catId2,now,new.id);
     insert into onboarding.task (title,priority_id,category_id,task_date,employee_id) values ('Ziele bis zum Ende der Probezeit mit dem Teamleiter & Geschäftsführung vereinbaren',priorId4,catId2,now,new.id);
     insert into onboarding.task (title,priority_id,category_id,task_date,employee_id) values ('Notwendige Tools und Software für die Arbeit auf dem Rechner installieren',priorId4,catId1,oneDay,new.id);
     insert into onboarding.task (title,priority_id,category_id,task_date,employee_id) values ('Alle entsprechende Rechte/Zugriffe von Repositories der Firma bekommen',priorId4,catId1,oneDay,new.id);
     insert into onboarding.task (title,priority_id,category_id,task_date,employee_id) values ('Grundlagen von Geschäftsprozessen einarbeiten',priorId4,catId1,oneWeek,new.id);
     insert into onboarding.task (title,priority_id,category_id,task_date,employee_id) values ('Begrüßungs-Kuchen für die Kollegen gerne zubereiten... :)',priorId4,catId3,threeDays,new.id);

     /*  Beim Einfügen eines neuen employee wird ihm eine neue Ermächtigung - "USER" zugewiessen */
     insert into onboarding.employee_powers (employee_id,power_id) values (new.id,powerId);

     RETURN NEW;
 END;
$$;
 0   DROP FUNCTION onboarding.new_employee();
       teamtasksplanning          postgres    false    7            �            1255    16410 :   reset_identity_id_from_all_table_after_employee_truncate()    FUNCTION     �  CREATE FUNCTION onboarding.reset_identity_id_from_all_table_after_employee_truncate() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
        alter sequence onboarding.employee_id_seq restart with 1;
        alter sequence onboarding.activity_id_seq restart with 1;
        alter sequence onboarding.stat_id_seq restart with 1;
        
        alter sequence onboarding.employee_powers_id_seq restart with 1;
        alter sequence onboarding.category_id_seq restart with 1;
        alter sequence onboarding.priority_id_seq restart with 1;
        alter sequence onboarding.task_id_seq restart with 1;

	RETURN OLD; 
END
$$;
 \   DROP FUNCTION onboarding.reset_identity_id_from_all_table_after_employee_truncate();
       teamtasksplanning          postgres    false    7            �            1255    16411 
   update_task()    FUNCTION     A  CREATE FUNCTION onboarding.update_task() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN

  /* Im Fall wenn completed von 0 zu 1 geändert wurde. category bleibt dabei ungeändert */
    IF (coalesce(old.completed,0)=0 and new.completed=1 and coalesce(old.category_id,0)=coalesce(new.category_id,0)) THEN    
    
        /* Bei einer unveränderten Kategorie verringert sich die Anzahl der Unvollständigen um 1, die Anzahl der Abgeschlossenen erhöht sich um 1 */
		update onboarding.category set uncompleted_count=(coalesce(uncompleted_count,0)-1),completed_count=(coalesce(completed_count,0)+1) where id=old.category_id and employee_id=old.employee_id;
        
		 /* Gesamte statistik muss geändert werden */
		update onboarding.stat set uncompleted_total=(coalesce(uncompleted_total,0)-1),completed_total=(coalesce(completed_total,0)+1) where employee_id=old.employee_id;
  
	END IF;
      
    /* Im Fall wenn completed von 1 zu 0 geändert wurde. category bleibt dabei ungeändert */
    IF (coalesce(old.completed,1)=1 and new.completed=0 and coalesce(old.category_id,0)=coalesce(new.category_id,0)) THEN    
 
		/* Bei einer unveränderten Kategorie verringert sich die Anzahl der Abgeschlossenen um 1, die Anzahl der unvollständigen um 1 erhöht*/
		update onboarding.category set completed_count=(coalesce(completed_count,0)-1),uncompleted_count=(coalesce(uncompleted_count,0)+1) where id=old.category_id and employee_id=old.employee_id;
       
		 /* Gesamte statistik muss geändert werden */
		update onboarding.stat set completed_total=(coalesce(completed_total,0)-1),uncompleted_total=(coalesce(uncompleted_total,0)+1) where employee_id=old.employee_id;

	END IF;
     
     /* Im Fall wenn category geändert wurde. completed bleibt ungeändert. completed=1  */
    IF (coalesce(old.category_id,0)<>coalesce(new.category_id,0) and coalesce(old.completed,1)=1 and new.completed=1) THEN    
    
		/* Für die alte Kategorie verringert sich die Anzahl der Abgeschlossenen um 1 */
		update onboarding.category set completed_count=(coalesce(completed_count,0)-1) where id = old.category_id and employee_id=old.employee_id;

		/* Für die neue Kategorie erhöht sich die Anzahl der abgeschlossenen Aufgaben um 1*/
		update onboarding.category set completed_count=(coalesce(completed_count,0)+1) where id = new.category_id and employee_id=old.employee_id;
	
		 /* Gesamte statistik bleibt ungeändert */
 
	END IF;
   
     /* Im Fall wenn category geändert wurde. completed bleibt ungeändert. completed=0  */
    IF (coalesce(old.category_id,0) <> coalesce(new.category_id,0) and coalesce(old.completed,0)=0  and new.completed=0) THEN    
    
		/* Für die alte Kategorie verringert sich die Anzahl der nicht Abgeschlossenen um 1 */
		update onboarding.category set uncompleted_count = (coalesce(uncompleted_count,0)-1) where id = old.category_id and employee_id=old.employee_id;

		/* Für die neue Kategorie wird die Anzahl der unvollständigen um 1 erhöht */
		update onboarding.category set uncompleted_count = (coalesce(uncompleted_count,0)+1) where id = new.category_id and employee_id=old.employee_id;
       
		 /* Gesamte statistik bleibt ungeändert */
      
	END IF;
	
  /* Im Fall wenn category geändert wurde. completed wird von 1 zu 0 geändert  */
    IF (coalesce(old.category_id,0)<>coalesce(new.category_id,0) and coalesce(old.completed,1)=1 and new.completed=0) THEN    
    
		/* Für die alte Kategorie verringert sich die Anzahl der Abgeschlossenen um 1 */
		update onboarding.category set completed_count=(coalesce(completed_count,0)-1) where id=old.category_id and employee_id=old.employee_id;
        
		/* Für die neue Kategorie wird die Anzahl der unvollständigen um 1 erhöht */
		update onboarding.category set uncompleted_count=(coalesce(uncompleted_count,0)+1) where id=new.category_id and employee_id=old.employee_id;

		 /* Gesamte statistik muss geändert werden */
		update onboarding.stat set uncompleted_total=(coalesce(uncompleted_total,0)+1),completed_total=(coalesce(completed_total,0)-1) where employee_id=old.employee_id;
       
	END IF;
    
        
  /* Im Fall wenn category geändert wurde. completed wird von 0 zu 1 geändert  */
    IF (coalesce(old.completed,0)=0 and new.completed=1 and coalesce(old.category_id,0)<>coalesce(new.category_id,0)) THEN    
    
		/* Für die alte Kategorie verringert sich die Anzahl der nicht Abgeschlossenen um 1 */
		update onboarding.category set uncompleted_count=(coalesce(uncompleted_count,0)-1) where id=old.category_id and employee_id=old.employee_id;
        
		/* Für die neue Kategorie erhöht sich die Anzahl der abgeschlossenen Aufgaben um 1 */
		update onboarding.category set completed_count=(coalesce(completed_count,0)+1) where id=new.category_id and employee_id=old.employee_id;
        
		 /* Gesamte statistik muss geändert werden */
		update onboarding.stat set uncompleted_total=(coalesce(uncompleted_total,0)-1),completed_total=(coalesce(completed_total,0)+1) where employee_id=old.employee_id;
	 	 
	END IF;

	RETURN NEW;
	
END;
$$;
 /   DROP FUNCTION onboarding.update_task();
       teamtasksplanning          postgres    false    7            �            1259    16412    activity    TABLE     �   CREATE TABLE onboarding.activity (
    id bigint NOT NULL,
    uuid text NOT NULL,
    activated smallint DEFAULT 0,
    employee_id bigint NOT NULL
);
 '   DROP TABLE teamtasksplanning.activity;
       teamtasksplanning         heap    postgres    false    7            �            1259    16418    activity_id_seq    SEQUENCE     �   ALTER TABLE teamtasksplanning.activity ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME teamtasksplanning.activity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            teamtasksplanning          postgres    false    217    7            �            1259    16419    category    TABLE     �   CREATE TABLE teamtasksplanning.category (
    id bigint NOT NULL,
    title text NOT NULL,
    completed_count bigint DEFAULT 0,
    uncompleted_count bigint DEFAULT 0,
    employee_id bigint NOT NULL
);
 '   DROP TABLE onboarding.category;
       teamtasksplanning         heap    postgres    false    7            �            1259    16426    category_id_seq    SEQUENCE     �   ALTER TABLE onboarding.category ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME onboarding.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            teamtasksplanning          postgres    false    219    7            �            1259    16427    employee    TABLE     w   CREATE TABLE onboarding.employee (
    id bigint NOT NULL,
    login text,
    password text,
    email text
);
 '   DROP TABLE teamtasksplanning.employee;
       teamtasksplanning         heap    postgres    false    7            �            1259    16432    employee_id_seq    SEQUENCE     �   ALTER TABLE teamtasksplanning.employee ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME teamtasksplanning.employee_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            teamtasksplanning          postgres    false    221    7            �            1259    16433    employee_powers    TABLE     �   CREATE TABLE teamtasksplanning.employee_powers (
    id bigint NOT NULL,
    employee_id bigint NOT NULL,
    power_id bigint NOT NULL
);
 .   DROP TABLE teamtasksplanning.employee_powers;
       teamtasksplanning         heap    postgres    false    7            �            1259    16436    employee_powers_id_seq    SEQUENCE     �   ALTER TABLE teamtasksplanning.employee_powers ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME teamtasksplanning.employee_powers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            teamtasksplanning          postgres    false    223    7            �            1259    16437    powers    TABLE     Z   CREATE TABLE teamtasksplanning.powers (
    id bigint NOT NULL,
    name text NOT NULL
);
 %   DROP TABLE teamtasksplanning.powers;
       teamtasksplanning         heap    postgres    false    7            �            1259    16442 
   powers_id_seq    SEQUENCE     �   ALTER TABLE teamtasksplanning.powers ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME teamtasksplanning.powers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            teamtasksplanning          postgres    false    7    225            �            1259    16443    priority    TABLE     �   CREATE TABLE teamtasksplanning.priority (
    id bigint NOT NULL,
    title text NOT NULL,
    color text,
    employee_id bigint NOT NULL
);
 '   DROP TABLE onboarding.priority;
       teamtasksplanning         heap    postgres    false    7            �            1259    16448    priority_id_seq    SEQUENCE     �   ALTER TABLE onboarding.priority ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME onboarding.priority_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            teamtasksplanning          postgres    false    7    227            �            1259    16449    stat    TABLE     �   CREATE TABLE onboarding.stat (
    id bigint NOT NULL,
    completed_total bigint DEFAULT 0,
    uncompleted_total bigint DEFAULT 0,
    employee_id bigint NOT NULL
);
 #   DROP TABLE onboarding.stat;
       teamtasksplanning         heap    postgres    false    7            �            1259    16454    stat_id_seq    SEQUENCE     �   ALTER TABLE onboarding.stat ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME onboarding.stat_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            teamtasksplanning          postgres    false    7    229            �            1259    16455    task    TABLE     	  CREATE TABLE onboarding.task (
    id bigint NOT NULL,
    title text NOT NULL,
    completed smallint DEFAULT 0,
    task_date timestamp without time zone,
    category_id bigint NOT NULL,
    priority_id bigint NOT NULL,
    employee_id bigint NOT NULL
);
 #   DROP TABLE onboarding.task;
       teamtasksplanning         heap    postgres    false    7            �            1259    16461    task_id_seq    SEQUENCE     �   ALTER TABLE onboarding.task ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME onboarding.task_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            teamtasksplanning          postgres    false    231    7                      0    16412    activity 
   TABLE DATA           O   COPY onboarding.activity (id, uuid, activated, employee_id) FROM stdin;
    teamtasksplanning          postgres    false    217   É                 0    16419    category 
   TABLE DATA           i   COPY onboarding.category (id, title, completed_count, uncompleted_count, employee_id) FROM stdin;
    teamtasksplanning          postgres    false    219   
�                 0    16427    employee 
   TABLE DATA           I   COPY onboarding.employee (id, login, password, email) FROM stdin;
    teamtasksplanning          postgres    false    221   f�       
          0    16433    employee_powers 
   TABLE DATA           O   COPY onboarding.employee_powers (id, employee_id, power_id) FROM stdin;
    teamtasksplanning          postgres    false    223   �                 0    16437    powers 
   TABLE DATA           5   COPY onboarding.powers (id, name) FROM stdin;
    teamtasksplanning          postgres    false    225   
�                 0    16443    priority 
   TABLE DATA           L   COPY onboarding.priority (id, title, color, employee_id) FROM stdin;
    teamtasksplanning          postgres    false    227   9�                 0    16449    stat 
   TABLE DATA           ^   COPY onboarding.stat (id, completed_total, uncompleted_total, employee_id) FROM stdin;
    teamtasksplanning          postgres    false    229   ��                 0    16455    task 
   TABLE DATA           q   COPY onboarding.task (id, title, completed, task_date, category_id, priority_id, employee_id) FROM stdin;
    teamtasksplanning          postgres    false    231   ��                  0    0    activity_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('teamtasksplanning.activity_id_seq', 774, true);
          teamtasksplanning          postgres    false    218                       0    0    category_id_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('teamtasksplanning.category_id_seq', 2523, true);
          teamtasksplanning          postgres    false    220                       0    0    employee_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('teamtasksplanning.employee_id_seq', 838, true);
          teamtasksplanning          postgres    false    222                       0    0    employee_powers_id_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('teamtasksplanning.employee_powers_id_seq', 838, true);
          teamtasksplanning          postgres    false    224                       0    0 
   powers_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('teamtasksplanning.powers_id_seq', 1, false);
          teamtasksplanning          postgres    false    226                        0    0    priority_id_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('teamtasksplanning.priority_id_seq', 4190, true);
          teamtasksplanning          postgres    false    228            !           0    0    stat_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('teamtasksplanning.stat_id_seq', 838, true);
          teamtasksplanning          postgres    false    230            "           0    0    task_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('teamtasksplanning.task_id_seq', 5866, true);
          teamtasksplanning          postgres    false    232            K           2606    16463    activity activity_pkey 
   CONSTRAINT     _   ALTER TABLE ONLY onboarding.activity
    ADD CONSTRAINT activity_pkey PRIMARY KEY (id);
 K   ALTER TABLE ONLY onboarding.activity DROP CONSTRAINT activity_pkey;
       teamtasksplanning            postgres    false    217            Q           2606    16465    category category_pkey 
   CONSTRAINT     _   ALTER TABLE ONLY onboarding.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);
 K   ALTER TABLE ONLY onboarding.category DROP CONSTRAINT category_pkey;
       teamtasksplanning            postgres    false    219            T           2606    16467    employee email_employee_uniq 
   CONSTRAINT     c   ALTER TABLE ONLY onboarding.employee
    ADD CONSTRAINT email_employee_uniq UNIQUE (email);
 Q   ALTER TABLE ONLY onboarding.employee DROP CONSTRAINT email_employee_uniq;
       teamtasksplanning            postgres    false    221            N           2606    16469 "   activity employee_id_activity_uniq 
   CONSTRAINT     o   ALTER TABLE ONLY onboarding.activity
    ADD CONSTRAINT employee_id_activity_uniq UNIQUE (employee_id);
 W   ALTER TABLE ONLY onboarding.activity DROP CONSTRAINT employee_id_activity_uniq;
       teamtasksplanning            postgres    false    217            Z           2606    16471 9   employee_powers employee_id_power_id_employee_powers_uniq 
   CONSTRAINT     �   ALTER TABLE ONLY onboarding.employee_powers
    ADD CONSTRAINT employee_id_power_id_employee_powers_uniq UNIQUE (employee_id, power_id);
 n   ALTER TABLE ONLY onboarding.employee_powers DROP CONSTRAINT employee_id_power_id_employee_powers_uniq;
       teamtasksplanning            postgres    false    223    223            b           2606    16473    stat employee_id_stat_uniq 
   CONSTRAINT     g   ALTER TABLE ONLY onboarding.stat
    ADD CONSTRAINT employee_id_stat_uniq UNIQUE (employee_id);
 O   ALTER TABLE ONLY onboarding.stat DROP CONSTRAINT employee_id_stat_uniq;
       teamtasksplanning            postgres    false    229            V           2606    16475    employee employee_pkey 
   CONSTRAINT     _   ALTER TABLE ONLY onboarding.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (id);
 K   ALTER TABLE ONLY onboarding.employee DROP CONSTRAINT employee_pkey;
       teamtasksplanning            postgres    false    221            \           2606    16477 $   employee_powers employee_powers_pkey 
   CONSTRAINT     m   ALTER TABLE ONLY onboarding.employee_powers
    ADD CONSTRAINT employee_powers_pkey PRIMARY KEY (id);
 Y   ALTER TABLE ONLY onboarding.employee_powers DROP CONSTRAINT employee_powers_pkey;
       teamtasksplanning            postgres    false    223            X           2606    16479    employee login_employee_uniq 
   CONSTRAINT     c   ALTER TABLE ONLY onboarding.employee
    ADD CONSTRAINT login_employee_uniq UNIQUE (login);
 Q   ALTER TABLE ONLY onboarding.employee DROP CONSTRAINT login_employee_uniq;
       teamtasksplanning            postgres    false    221            ^           2606    16481    powers power_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY onboarding.powers
    ADD CONSTRAINT power_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY onboarding.powers DROP CONSTRAINT power_pkey;
       teamtasksplanning            postgres    false    225            `           2606    16483    priority priority_pkey 
   CONSTRAINT     _   ALTER TABLE ONLY onboarding.priority
    ADD CONSTRAINT priority_pkey PRIMARY KEY (id);
 K   ALTER TABLE ONLY onboarding.priority DROP CONSTRAINT priority_pkey;
       teamtasksplanning            postgres    false    227            d           2606    16485    stat stat_pkey 
   CONSTRAINT     W   ALTER TABLE ONLY onboarding.stat
    ADD CONSTRAINT stat_pkey PRIMARY KEY (id);
 C   ALTER TABLE ONLY onboarding.stat DROP CONSTRAINT stat_pkey;
       teamtasksplanning            postgres    false    229            g           2606    16487    task task_pkey 
   CONSTRAINT     W   ALTER TABLE ONLY onboarding.task
    ADD CONSTRAINT task_pkey PRIMARY KEY (id);
 C   ALTER TABLE ONLY onboarding.task DROP CONSTRAINT task_pkey;
       teamtasksplanning            postgres    false    231            e           1259    16488    categoryid_index    INDEX     S   CREATE INDEX categoryid_index ON onboarding.task USING btree (category_id);
 /   DROP INDEX onboarding.categoryid_index;
       teamtasksplanning            postgres    false    231            R           1259    16489    categorytitle_index    INDEX     T   CREATE INDEX categorytitle_index ON onboarding.category USING btree (title);
 2   DROP INDEX onboarding.categorytitle_index;
       teamtasksplanning            postgres    false    219            L           1259    16490    employee_activated_idx    INDEX     [   CREATE INDEX employee_activated_idx ON onboarding.activity USING btree (activated);
 5   DROP INDEX onboarding.employee_activated_idx;
       teamtasksplanning            postgres    false    217            O           1259    16491    employee_uuid_idx    INDEX     Q   CREATE INDEX employee_uuid_idx ON onboarding.activity USING btree (uuid);
 0   DROP INDEX onboarding.employee_uuid_idx;
       teamtasksplanning            postgres    false    217            h           1259    16492    title_index    INDEX     H   CREATE INDEX title_index ON onboarding.task USING btree (title);
 *   DROP INDEX onboarding.title_index;
       teamtasksplanning            postgres    false    231            r           2620    16493    task add_task_stat    TRIGGER     �   CREATE TRIGGER add_task_stat AFTER INSERT ON onboarding.task FOR EACH ROW EXECUTE FUNCTION onboarding.add_task();
 6   DROP TRIGGER add_task_stat ON onboarding.task;
       teamtasksplanning          postgres    false    244    231            s           2620    16494    task delete_task_stat    TRIGGER     �   CREATE TRIGGER delete_task_stat BEFORE DELETE ON onboarding.task FOR EACH ROW EXECUTE FUNCTION onboarding.delete_task();
 9   DROP TRIGGER delete_task_stat ON onboarding.task;
       teamtasksplanning          postgres    false    231    245            p           2620    16495    employee new_employee    TRIGGER     �   CREATE TRIGGER new_employee AFTER INSERT ON onboarding.employee FOR EACH ROW EXECUTE FUNCTION onboarding.new_employee();
 9   DROP TRIGGER new_employee ON onboarding.employee;
       teamtasksplanning          postgres    false    221    246            q           2620    16496 A   employee reset_identity_id_from_all_table_after_employee_truncate    TRIGGER     �   CREATE TRIGGER reset_identity_id_from_all_table_after_employee_truncate AFTER TRUNCATE ON onboarding.employee FOR EACH STATEMENT EXECUTE FUNCTION onboarding.reset_identity_id_from_all_table_after_employee_truncate();
 e   DROP TRIGGER reset_identity_id_from_all_table_after_employee_truncate ON onboarding.employee;
       teamtasksplanning          postgres    false    247    221            t           2620    16497    task update_task_stat    TRIGGER     �   CREATE TRIGGER update_task_stat AFTER UPDATE ON onboarding.task FOR EACH ROW EXECUTE FUNCTION onboarding.update_task();
 9   DROP TRIGGER update_task_stat ON onboarding.task;
       teamtasksplanning          postgres    false    231    248            i           2606    16498    activity employee_fkey 
   FK CONSTRAINT     �   ALTER TABLE ONLY onboarding.activity
    ADD CONSTRAINT employee_fkey FOREIGN KEY (employee_id) REFERENCES onboarding.employee(id) ON DELETE CASCADE;
 K   ALTER TABLE ONLY onboarding.activity DROP CONSTRAINT employee_fkey;
       teamtasksplanning          postgres    false    217    221    4694            j           2606    16503    category employee_fkey 
   FK CONSTRAINT     �   ALTER TABLE ONLY onboarding.category
    ADD CONSTRAINT employee_fkey FOREIGN KEY (employee_id) REFERENCES onboarding.employee(id) ON DELETE CASCADE;
 K   ALTER TABLE ONLY onboarding.category DROP CONSTRAINT employee_fkey;
       teamtasksplanning          postgres    false    219    4694    221            k           2606    16508    employee_powers employee_fkey 
   FK CONSTRAINT     �   ALTER TABLE ONLY onboarding.employee_powers
    ADD CONSTRAINT employee_fkey FOREIGN KEY (employee_id) REFERENCES onboarding.employee(id) ON DELETE CASCADE;
 R   ALTER TABLE ONLY onboarding.employee_powers DROP CONSTRAINT employee_fkey;
       teamtasksplanning          postgres    false    221    4694    223            m           2606    16513    priority employee_fkey 
   FK CONSTRAINT     �   ALTER TABLE ONLY onboarding.priority
    ADD CONSTRAINT employee_fkey FOREIGN KEY (employee_id) REFERENCES onboarding.employee(id) ON DELETE CASCADE;
 K   ALTER TABLE ONLY onboarding.priority DROP CONSTRAINT employee_fkey;
       teamtasksplanning          postgres    false    227    4694    221            n           2606    16518    stat employee_fkey 
   FK CONSTRAINT     �   ALTER TABLE ONLY onboarding.stat
    ADD CONSTRAINT employee_fkey FOREIGN KEY (employee_id) REFERENCES onboarding.employee(id) ON DELETE CASCADE;
 G   ALTER TABLE ONLY onboarding.stat DROP CONSTRAINT employee_fkey;
       teamtasksplanning          postgres    false    4694    221    229            o           2606    16523    task employee_fkey 
   FK CONSTRAINT     �   ALTER TABLE ONLY onboarding.task
    ADD CONSTRAINT employee_fkey FOREIGN KEY (employee_id) REFERENCES onboarding.employee(id) ON DELETE CASCADE;
 G   ALTER TABLE ONLY onboarding.task DROP CONSTRAINT employee_fkey;
       teamtasksplanning          postgres    false    231    221    4694            l           2606    16528    employee_powers power_fkey 
   FK CONSTRAINT     �   ALTER TABLE ONLY onboarding.employee_powers
    ADD CONSTRAINT power_fkey FOREIGN KEY (power_id) REFERENCES onboarding.powers(id) ON DELETE RESTRICT;
 O   ALTER TABLE ONLY onboarding.employee_powers DROP CONSTRAINT power_fkey;
       teamtasksplanning          postgres    false    223    4702    225               :   x����0�7�s�MHz��/!��ՑĤ��6nl�L���U�����C�KUSr          I   x�324��IM��MM-��K/�4�4�474�2246�t��K,JJ�,)�K��d,9=KJJ��:b���� |�t         r   x�374�L�K)J���O���T1JT14P�*�(.0r��v�v�r)p�
֫����׳p+(5.r���)	�3�r2���,�	�����Y�������[����������� ��#[      
      x�374�4bC�=... d&            x�3�
v
�2�tt�������� ,>�         n   x�5̱
�@��9��풒Ğo �]\]j�xD�����]�~>��-���]�	�p1���K1�z��k���ŷz��ն���Ip�s�w�?;$\�}J�� �            x�374�4�4�474������ le         �  x���Mn�0���)fU� ��vT�,���e��q�Ibձ�퀔�t�c��b�����JY�X��f�d��P�`fR�n$w
T��\���

����
Y?�ǝ0�D�i��~4H�`'l�A���l� #�;,�ax�U�[�"�D�+�d�N��(�jH����PH��U��8bk�!����ʰ�}4:ņ����H�,I��7�@�Z>rg�þ44&l�P�������A��L+���Ze�s�# o���y;�O�BYǥ����1�g�	�I
���f�}bo谷�#�a�im��F�m+�����^�%lA�d��&�{�nct�֒N��6�ox�w����e�[jJ鑅��m��C��n������A�t� ����A     
