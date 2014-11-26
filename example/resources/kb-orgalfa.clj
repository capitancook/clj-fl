[
;-------------------------------------------Knowledge Base start------------------------------------------------


;------ Frame Language

 {:frame {:value "frame"}
  :description {:value "A frame is composed of one or more slots. Each frame represent a concept or a class of object or an object"}}
 {:frame {:value "slot"}
  :description {:value "One or more slots form a frame. Each slot represent a property or a feature of the frame"}}
 {:frame {:value "facet"}
  :description {:value "One or more facet form a slot. Each facet represent the content or the rule governing the content of a slot"}}

;------ AKO frames

 {:frame {:value "person"}
  :description {:value "A human being"}
  :showcofs {:proc showcofs}}

 {:frame {:value "structure"}
  :description {:value "A social structure of human beings aimed at reaching some target"}
  :showcofs {:proc showcofs}
  :delete {:proc delstruct}

 {:frame {:value "level"}
  :description {:value "A natural, social or conceptual level. In this example is a hierarchical level for people working in the organization "}}

;------ ISA frames

;; Structures

 {:frame {:value  "organization"}
  :ako {:value "structure"}}

 {:frame {:value  "dg"}
  :ako {:value "structure"}}

 {:frame {:value  "department"}
  :ako {:value "structure"}}

 {:frame {:value  "unit"}
  :ako {:value "structure"}}

;; Persons

 {:frame {:value "male"}
  :ako   {:value "person"}
  :height{:default 1.80}}

 {:frame {:value "female"}
  :ako   {:value "person"}
  :height{:default 1.70}}

;;  {:frame {:value "employee"}
;;   :ako {:value "person"}}

;; levels

 {:frame {:value "AST1"}
  :salary {:value 2000.00}
  :ako {:value "level"}} ; Assistants (AST);
 {:frame {:value "AST2"}
  :salary {:value 2100.00}
  :ako {:value "level"}}
 {:frame {:value "AST3"}
  :salary {:value 2200.00}
  :ako {:value "level"}}
 {:frame {:value "AST4"}
  :salary {:value 2200.00}
  :ako {:value "level"}}
 {:frame {:value "AST5"}
  :salary {:value 2300.00}
  :ako {:value "level"}}
 {:frame {:value "AST-SC1"}
  :salary {:value 3000.00}
  :ako {:value "level"}} ;Secretaries/clerks (AST/SC)
 {:frame {:value "AST-SC2"}
  :salary {:value 3100.00}
  :ako {:value "level"}}
 {:frame {:value "AST-SC3"}
  :salary {:value 3200.00}
  :ako {:value "level"}}
 {:frame {:value "AST-SC4"}
  :salary {:value 3300.00}
  :ako {:value "level"}}
 {:frame {:value "AST-SC5"}
  :salary {:value 3400.00}
  :ako {:value "level"}}
 {:frame {:value "AD5"}
  :salary {:value 4000.00}
  :ako {:value "level"}} ; Administrators (AD)
 {:frame {:value "AD6"}
  :salary {:value 4500.00}
  :ako {:value "level"}}
 {:frame {:value "AD7"}
  :salary {:value 5000.00}
  :ako {:value "level"}}
 {:frame {:value "AD8"}
  :salary {:value 5500.00}
  :ako {:value "level"}}
 {:frame {:value "AD9"}
  :salary {:value 6000.00}
  :ako {:value "level"}}
 {:frame {:value "AD10"}
  :salary {:value 6500.00}
  :ako {:value "level"}}
 {:frame {:value "AD11"}
  :salary {:value 7000.00}
  :ako {:value "level"}}
 {:frame {:value "AD12"}
  :salary {:value 7500.00}
  :ako {:value "level"}}
 {:frame {:value "AD13"}
  :salary {:value 8000.00}
  :ako {:value "level"}}
 {:frame {:value "AD14"}
  :salary {:value 8500.00}
  :ako {:value "level"}}
 {:frame {:value "AD15"}
  :salary {:value 9000.00}
  :ako {:value "level"}}
 {:frame {:value "AD16"}
  :salary {:value 10000.00}
  :ako {:value "level"}}

;; Job titles

 {:frame {:value "Assistant"}
  :ako   {:value "person"}
  :perks {:value 0.1}
  :bonus {:default 3000.00}}
 {:frame {:value "Secretary"}
  :ako   {:value "person"}
  :perks {:value 0.1}
  :bonus {:default 3000.00}}
 {:frame {:value "Administrator"}
  :ako   {:value "person"}
  :perks {:value 0.2}
  :bonus {:default 10000.00}}
 {:frame {:value "Unit-head"}
  :ako   {:value "person"}
  :perks {:value 0.3}
  :bonus {:value 25000.00}}
 {:frame {:value "Department-head"}
  :ako {:value "person"}
  :perks {:value 0.4}
  :bonus {:default 45000.00}}
 {:frame {:value "Dg-head"}
  :ako {:value "person"}
  :perks {:value 0.5}
  :bonus {:default 60000.00}}
 {:frame {:value "Org-head"}
  :ako {:value "person"}
  :perks {:value 0.65}
  :bonus {:default 100000.00}}

;------ object frames

;; Structures

 {:frame {:value "OrgAlfa"}
  :name {:value "Organisation Alfa"}
  :cof {:value ("DGA" "DGB" "E16" "E17" "E18")}
  :isa {:value "organization"}
  :status {:value "active"}}

 {:frame {:value "DGA"}
  :name {:value "General Directorate A"}
  :cof {:value ("D1" "D2")}
  :isi {:value "OrgAlfa"}
  :isa {:value "dg"}
  :status {:value "active"}}
 {:frame {:value "DGB"}
  :name  {:value "General Directorate B"}
  :cof {:value ("D3")}
  :isi {:value "OrgAlfa"}
  :isa {:value "dg"}
  :status {:value "active"}}

 {:frame {:value  "D1"}
  :name {:value "Department 1"}
  :cof {:value ("U1" "U2")}
  :isa {:value "department"}
  :status {:value "active"}}
 {:frame {:value "D2"}
  :name {:value "Department 2"}
  :cof {:value ("U3")}
  :isa {:value "department"}
  :status {:value "active"}}
 {:frame {:value "D3"}
  :name {:value "Department 3"}
  :cof {:value ("U4")}
  :isa {:value "department"}
  :status {:value "active"}}

 {:frame {:value "U1"}
  :name {:value "Unit 1"}
  :cof {:value ("E1" "E2" "E3" "E4")}
  :isa {:value "unit"}
  :status {:value "active"}}
 {:frame {:value "U2"}
  :name {:value "Unit 2"}
  :cof {:value ("E5" "E6" "E7")}
  :isa {:value "unit"}
  :status {:value "active"}}
 {:frame {:value "U3"}
  :name {:value "Unit 3"}
  :cof {:value ("E8" "E9" "E10" "E11" "E12")}
  :isa {:value "unit"}
  :status {:value "active"}}
 {:frame {:value "U4"}
  :name {:value "Unit 4"}
  :cof {:value ("E13" "E14" "E15")}
  :isa {:value "unit"}
  :status {:value "active"}}

;; Persons

 {:frame {:value "E1"}
  :id {:value 1}
  :recruitment-date {:value "19991102"}
  :surname {:value "Rossi"}
  :name {:value "Paolo"}
  :address {:value {:street "Via del Sudario, 8" :city "Rome" :postalcode 00100}}
  :isi {:value "U1"}
  :isa {:value "male"}
;  :isa {:value ("male" "Unit-head")}
  :status {:value "active"}
  :level {:value "AD8"}}
 {:frame {:value "E2"}
  :id {:value 2}
  :recruitment-date {:value "20000104"}
  :surname {:value "Doe"}
  :name {:value "John"}
  :address {:value {:street "Brown street, 345" :city "London" :postalcode 00330}}
  :isi {:value "U1"}
  :isa {:value ("male" "secretary")}
  :status {:value "active"}
  :level {:value "AST-SC2"}}
 {:frame {:value "E3"}
  :id {:value 3}
  :recruitment-date {:value "20010213"}
  :surname {:value "Dupont"}
  :name {:value "Arthur "}
  :address {:value {:street "Rue du Temple, 68" :city "Paris" :postalcode 00440}}
  :isi {:value "U1"}
  :isa {:value ("male" "Administrator")}
  :status {:value "active"}
  :level {:value "AD6"}}
 {:frame {:value "E4"}
  :id {:value 4}
  :recruitment-date {:value "19991102"}
  :surname {:value "Verdi"}
  :name {:value "Maria"}
  :isi {:value "U1"}
  :isa {:value ("female" "Administrator")}
  :status {:value "active"}
  :level {:value "AD5"}}

 {:frame {:value "E5"}
  :id {:value 5}
  :recruitment-date {:value "19991102"}
  :surname {:value "Bianchi"}
  :name {:value "Giulia"}
  :isi {:value "U2"}
  :isa {:value ("female" "Unit-head")}
  :status {:value "active"}
  :level {:value "AD10"}}
 {:frame {:value "E6"}
  :id {:value 6}
  :recruitment-date {:value "19991102"}
  :surname {:value "Mussi"}
  :name {:value "Loredana"}
  :isi {:value "U2"}
  :isa {:value ("female" "Secretary")}
  :status {:value "active"}
  :level {:value "AST-SC3"}}
 {:frame {:value "E7"}
  :id {:value 7}
  :recruitment-date {:value "19991102"}
  :surname {:value "Schubert"}
  :name {:value "Franz"}
  :isi {:value "U2"}
  :isa {:value ("male" "Assistant")}
  :status {:value "active"}
  :level {:value "AST3"}}

 {:frame {:value "E8"}
  :id {:value 8}
  :recruitment-date {:value "19991102"}
  :surname {:value "Pellet"}
  :name {:value "Anne"}
  :isi {:value "U3"}
  :isa {:value ("female" "Unit-head")}
  :status {:value "active"}
  :level {:value "A10"}}
 {:frame {:value "E9"}
  :id {:value 9}
  :recruitment-date {:value "19991102"}
  :surname {:value "García"}
  :name {:value "Adoracion"}
  :isi {:value "U3"}
  :isa {:value ("female" "Secretary")}
  :status {:value "active"}
  :level {:value "AST-SC3"}}
 {:frame {:value "E10"}
  :id {:value 10}
  :recruitment-date {:value "19991102"}
  :surname {:value "Smith"}
  :name {:value "Robert"}
  :isi {:value "U3"}
  :isa {:value ("male" "Secretary")}
  :status {:value "active"}
  :level {:value "AST-SC2"}}
 {:frame {:value "E11"}
  :id {:value 11}
  :recruitment-date {:value "19991102"}
  :surname {:value "Poe"}
  :name {:value "Edgar Allan"}
  :isi {:value "U3"}
  :isa {:value ("male" "Administrator")}
  :status {:value "active"}
  :level {:value "AD6"}}
 {:frame {:value "E12"}
  :id {:value 12}
  :recruitment-date {:value "19991102"}
  :surname {:value "Peck"}
  :name {:value "Gregory"}
  :isi {:value "U3"}
  :isa {:value ("male" "administrator")}
  :status {:value "active"}
  :level {:value "AD5"}}

 {:frame {:value "E13"}
  :id {:value 13}
  :recruitment-date {:value "19991102"}
  :surname {:value "Errante"}
  :name {:value "Anna"}
  :isi {:value "U4"}
  :isa {:value ("female" "Unit-head")}
  :status {:value "active"}
  :level {:value "AD11"}}
 {:frame {:value "E14"}
  :id {:value 14}
  :recruitment-date {:value "19991102"}
  :surname {:value "Müller"}
  :name {:value "Karen"}
  :isi {:value "U4"}
  :isa {:value ("female" "Secretary")}
  :status {:value "active"}
  :level {:value "AST-SC3"}}
 {:frame {:value "E15"}
  :id {:value 15}
  :recruitment-date {:value "19991102"}
  :surname {:value "Schmidt"}
  :name {:value "Franz"}
  :isi {:value "U4"}
  :isa {:value ("male" "Administrator")}
  :status {:value "active"}
  :level {:value "AD7"}}

 {:frame {:value "E16"}
  :id {:value 16}
  :recruitment-date {:value "19991102"}
  :surname {:value "Winston"}
  :name {:value "Henry Patrick"}
  :isi {:value 'OrgAlfa}
  :isa {:value ("male" "Org-head")}
  :status {:value "active"}
  :level {:value "AD16"}}
 {:frame {:value "E17"}
  :id {:value 17}
  :recruitment-date {:value "19991102"}
  :surname {:value "Kramer"}
  :name {:value "Edward"}
  :isi {:value "OrgAlfa"}
  :isa {:value ("male" "Secretary")}
  :status {:value "active"}
  :level {:value "AST-SC5"}}
 {:frame {:value "E18"}
  :id {:value 18}
  :recruitment-date {:value "19991102"}
  :surname {:value "Shnaider"}
  :name {:value "Helbert"}
  :isi {:value "OrgAlfa"}
  :isa {:value ("male" "Administrator")}
  :status {:value "active"}
  :level {:value "AD9"}}

 ;-------------------------------------------Knowledge Base end--------------------------------------------------

 ]
