(ns mini.playground
  (:import [java.time LocalDateTime]))

; This project has custom configuration.
; See .vscode/settings.json

; If you are new to Calva, you may want to use the command:
; Calva: Create a “Getting Started” REPL project
; which creates a project with a an interactive Calva (and Clojure) guide.

(println "Hello world!")

(+ 1 1) ;; 2
(* (+ 5 4) 2) ;; 18

(defn addd [a b]
  (+ a b))
(addd 1 2) ;; 3

(defn hello-name
  "Says hello to the given name"
  [name]
  (str "Hello" name))
(hello-name "João") ;; Hello João

;; All data structures in clojure are IMMUTABLE
(def colors ["red", "blue"])
(print colors)
(conj colors "black") ;; Adds black to the end of the list and returns the new lis

(def new-colors (conj colors "black"))
(print colors)
(print new-colors)

(defn greetings
  "Greetings based on time"
  []
  (let [now (LocalDateTime/now)]
    (if (< (.getHour now) 12)
      "Good morning"
      "God afternoon")))
(greetings)

(defn is-even-or-odd
  "Classify a number as even or odd"
  [number]
  (if (= (mod number 2) 0)
    "Even"
    "Odd"))
(is-even-or-odd 10) ;; Even
(is-even-or-odd 3) ;; Odd

(defn is-baby-kid-young-or-adult
  "Classify a person based on the age"
  [age]
  (cond
    (< age 1) "Baby"
    (< age 18) "Kid"
    (< age 24) "Young"
    (>= age 25) "Adult"))
(is-baby-kid-young-or-adult 0.8)
(is-baby-kid-young-or-adult 4)
(is-baby-kid-young-or-adult 19)
(is-baby-kid-young-or-adult 55)

;; Pure Functions
(def add
  (fn [number increment] (+ number increment)))
(defn add-one
  [number]
  (add number 1))
(add-one 5) ;; 6

(map (fn [name] (str "Hi, " name "!")) ["Gutz" "Casca"])
(map #(str "Hi, " %1 "!") ["Gutz" "Casca"])

;; Multiple parameters
(defn say-my-name
  ([name]
   (str "Hello, " name))
  ([first-name last-name]
   (str "Hello, " first-name " " last-name)))
(say-my-name "Ronaldo")
(say-my-name "Walter" "White")

;; VarArgs
(defn say-my-name-2
  [& names]
  (clojure.string/join " " (cons "Hello," names)))
(say-my-name-2 "João" "Victor" "Claudino" "Felipe")

(defn say-my-name-3
  [first-name & names]
  (clojure.string/join " " (conj names (clojure.string/upper-case first-name) "Hello,"))) ;; Since the parameters are dectured, conj added elements at the beggining
(say-my-name-3 "João" "Victor" "Claudino" "Felipe")

(defn say-my-name-4
  [& [first-name & names :as names-list]]
  (println "Parameter count:" (count names-list))
  (clojure.string/join " " (conj names (clojure.string/upper-case first-name) "Hello,")))
(say-my-name-4 "João" "Victor" "Claudino" "Felipe")
;; Tip: & separates many arguments as u want from the rest and the :as alias bonds all the arguments together in the name-list parameter

(defn say-my-name-5
  [person]
  (let [values (remove nil? ["Hello," (:first-name person) (:last-name person)])]
    (clojure.string/join " " values)))
(say-my-name-5 {:first-name "Walter" :last-name "White"})

(defn say-my-name-6
  [{first-name :first-name last-name :last-name}]
  (let [values (remove nil? ["Hello," first-name last-name])]
    (clojure.string/join " " values)))
(say-my-name-6 {:first-name "Walter" :last-name "White"})

(defn say-my-name-7 ;; Case the parameter has the same name of the key
  [{:keys [first-name last-name]}]
  (let [values (remove nil? ["Hello," first-name last-name])]
    (clojure.string/join " " values)))
(say-my-name-7 {:first-name "Walter" :last-name "White"})

(defn say-my-name-8
  [person]
  (let [default {:first-name "Jhon" :last-name "Doe"}
        merged (merge default person)
        values (remove nil? ["Hello," (:first-name merged) (:last-name merged)])]
    (clojure.string/join " " values)))
(say-my-name-8 {:first-name "Walter"})

(defn say-my-name-9
  [{:keys [first-name last-name] :or {first-name "Jhon" last-name "Doe"} :as person}]
  (println "Person has" (count person) "keys!")
  (let [values (remove nil? ["Hello," first-name last-name])]
    (clojure.string/join " " values)))
(say-my-name-9 {:first-name "Walter" :last-name "White"})
