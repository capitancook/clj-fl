# Frame Language in Clojure

[Frame Language (FL)](http://en.wikipedia.org/wiki/Frame_language) is a framework developed  in the seventies to represent chunks of knowledge, see Prof. Marvin Minsky seminal paper ["A framework for representing knowledge"](http://web.media.mit.edu/~minsky/papers/Frames/frames.html). The first implementations of FL were based on Lisp. The interest about FL survived to all the "fashions of the moment" of the last four decades of AI research. It is actively used in knowledge representation for semantic Web. Even Clojure has been clearly and strongly influenced by this paradigm.

This library is an attempt to bring in the Clojure ecosystem the simple and clear  Lisp formalization of the Frame Language introduced by Prof. Winston, in his great book  ["Lisp"](http://people.csail.mit.edu/phw/Books/#Lisp) .

At the moment (20141112) this library is a pre alfa software.

## What is a Frame?

Lets have a look at the implementation of a Frame as proposed by Prof. Winston as a simple associative nested  list in LISP:

```lisp

(Henry (isa (value system-analyst))
       (working-at(value unit-i ))
       (recruitment-date (value "14/03/2010"))
       (gross-salary (value 3000,00)))
```
Probably, many Clojurians are now thinking: “Hey! Came on! We can do that using  associative maps. At the end, these Frames are only property lists”.
That’s not wrong. For example, our old fellow Henry can be described using a nested associative map as follows:

```clojure
(def Henry {:isa {:value 'system-analyst}
            :working-at {:value 'unit-i}
            :recruitment-date {:value "14/03/2010"}
            :gross-salary {:value 3000,00}})
```
Please, note that system-analyst is itself a frame containing the FL description of a generic employee that works as a system analyst and, for example, has a certain base salary, health insurance, production bonus and other benefits.

To get Henry’s salary we can simply do:
```clojure
(-> Henry :gross-salary :value)
```
Frames are not only associative data structures. For example, if we have another person named John that, at the time we are writing his description in FL, has an unknown salary:
```clojure
(def John {:isa {:value 'system-analyst}
           :working-at {:value 'unit-i}
           :years-in-the-role {:value 5}})
```
If we try to use the same approach we used for Henry to get the salary information from the John's FL description we'll fail miserably.
We could estimate John's base salary using some general knowledge about the base salary of a System Analyst with 5 years of experience in the role of system-analyst.

If we can get elsewhere what was the average salary for a system analyst, for example 2000,00 EURO, and if it is  known that the annual increment is 3%, we could calculate John's base salary as follows:
```
gross-salary = system-analyst-salary*(1+0,03)^(years-in-the-role)
```
And, if we could store somewhere the above formula, when we need to know John's wage we could, because John :isa 'system-analyst, adopt the the previous formula to estimate his basic wage.
In this case the simple property list we used to represent a person features is not enough.
The FL extends the power of a property list adding more sophisticated mechanisms as: default values, demons and inheritance.

Hence, we can say that:

- A frame is a nested "augmented"  association list;
- A frame can represent a class of object or an object; in our example system-analyst is a frame representing a class of employees while Henry frame representing an instance of system-analyst;
- A frame is described/composed by one or more facts called slot;  in our example :net-monthly-salary is a slot;
- A slot is described/composed by one or more information called facet; in our example :value is a facet.

## Examples

The repo includes two simple knowledge bases as examples you can play with.
Assuming you have [Leiningen](http://leiningen.org/) installed, start a REPL in the example directory with:

```
lein repl
```

Then, at the repl prompt, type

```clojure
(showorgalfa)

```
to load the OrgAlfa use case, where the frame language is used to model a simple knowledge based ERP. The OrgAlfa  is a hierarchical organisation similar to the European Commission structure.

Or you can type:

```clojure
(showkbcad)
```

to load the kbcad use case, a simple knowledge based Computer Aided Design system.

After each of the previous command/function you can play around with the KB you loaded.

## Using it

### Please note that clj-fl is pre-alpha software.

Current version of clj-fl is:

[![Clojars Project](http://clojars.org/clj-fl/latest-version.svg)](http://clojars.org/clj-fl)

Even if this library is still not ready for production to use it, assuming you have [Leiningen](http://leiningen.org/) installed, your project.clj should include something like the following:

```clojure
(defproject foo "0.1.0-SNAPSHOT"
  ;...
  :dependencies [[org.clojure/clojure "1.6.1"]
                 [clj-fl "0.1.0-prealfa5"] ; please check on Clojars.org for the latest version
                 ...]
  ;...
  )
```

## Documention

Still working on this :) For the moment, please check the code docstrings for the existing functionality.

## License

Copyright © 2014 Luciano Capitanio

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
