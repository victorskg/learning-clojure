(ns mini.domain
  (:require [clojure.spec.alpha :as s]))

;; Whe use map to represent objects ins the majority of time, but not 100%. Some times we use records
(def person {:name "Joao"
             :age 26
             :state "CE"})
(print person)
(get person :age) ;; Get att value from map ->> 26
(person :age) ;; 26
(:age person) ;; 26

(defn is-baby-kid-young-or-adult
  "Classify a person based on the age"
  [age]
  (cond
    (< age 1) "Baby"
    (< age 18) "Kid"
    (< age 24) "Young"
    (>= age 25) "Adult"))
(-> person
    :age
    is-baby-kid-young-or-adult) ;; -> Passes the return of a function to the next functions parameter
(map :name [person]) ;; Extracts value in map (:name is a function who return the value of the att)

;; Record
(defrecord Person [name
                   age
                   state])
(def ronaldo (->Person "Ronaldo"
                       55
                       "SP")) ;; Positional factory used when whe have all values
(:name ronaldo)

(def ronaldinho (map->Person {:name "Ronaldinho"
                              :age 46
                              :state "RS"})) ;; Map factory
(:name ronaldinho)
(def new-ronaldinho (assoc ronaldinho :random-data "Some random data"))
(println ronaldinho new-ronaldinho) ;; ronaldinho is immutable

;; Relationship by nesting
;; In this case, we update entities together and they share the same lifecycle (one cannot exist without the other)
{:customer-id 12345
 :first-name  "Maria"
 :last-name   "da Silva"
 :accounts [{:account-id "111-222-333"
             :account-type :checking}
            {:account-id "111-222-335"
             :account-type :credit}]} ;; Customer is the main entity, so sit has its accounts list
{:account-id "111-222-333"
 :account-type :checking
 :customer {:customer-id 12345
            :first-name  "Maria"
            :last-name   "da Silva"}} ;; In this vision, account is the main entity

;; Relationship by reference
(def currencies {:usd {:divisor 100  :symbol "USD"}
                 :brl {:divisor 100  :symbol "BRL"}}) ;; Enum
(:usd currencies)

; Dinheiro (especificamente, $12.00 USD)
{:amount 1200
 :currency :usd} ;; :usd is a reference

;; Ref for log lofecycle objects
(def customers {12345 (ref {:customer-id 12345
                            :first-name  "Maria"
                            :last-name   "da Silva"})
                12346 (ref {:customer-id 12346
                            :first-name  "John"
                            :last-name   "Smith"})})
(def maria (get customers 12345))
(print maria) ;; This is a ref
(:first-name @maria) ;; Accessing the value of :first-name
(dosync (alter (get customers 12345) assoc :first-name "Gabriela")) ;; dosync alter will change the previous constant maria, since it is a reference

(-> (get customers 12345)
    (alter assoc :first-name "Helena")
    (dosync)) ;; This is the same as the previous :D Its sooo much nicer

(:first-name @maria) ;; Helena

;;

(def currencies2 {:usd {:divisor 100 :code "USD" :sign "$" :desc "US Dollars"}
                  :brl {:divisor 100 :code "BRL" :sign "R$" :desc "Brazilian Real"}
                  :ukg {:divisor (* 17 29) :code "UKG" :sign "ʛ" :desc "Galleons of the United Kingdom"}})

; Money (especificamente, $12.00 USD)
{:amount 1200
 :currency :usd}

;; Constructor with default values
(def default-currency (:brl currencies2))

(defn make-money
  "Creates a money entity"
  ([]                   {:amount 0
                         :currency default-currency})
  ([amount]             {:amount amount
                         :currency default-currency})
  ([amount currency]    {:amount amount
                         :currency currency}))

(defn make-money-2 [amount currency]
  (let [am (if (not amount) 0 amount)
        cr (if (not currency) default-currency currency)
        money {:amount am :currency cr}]
    (assoc money :display (str (:sign cr) am " of " (:desc cr)))))
(make-money-2 10 (:usd currencies2))

;; Namespace
;; use: inclui funções de outros namespaces
;; import: inclui classes, tipos e símbolos de outros namespaces
;; require: inclui outros namespaces dentro do namespace atual

;; Validation
(s/def :currency/divisor int?) ;; Should be int
(s/def :currency/sign string?) ;; Should be string
(s/def :currency/code (and string? #{"USD" "BRL" "UKG"})) ;; Should be string AND one of the elements on the set
(s/def :currency/desc (s/nilable string?)) ;; Its optional but if informed should be a string

(s/def :domain/currency (s/keys :req-un [:currency/divisor
                                         :currency/code
                                         :currency/sign]
                                :opt-un [:currency/desc]))

(s/valid? :domain/currency (:usd currencies2)) ;; true
(s/valid? :domain/currency (:usd currencies)) ;; false since currencies does not have sign

(map #(s/valid? :finance/currency %) (vals currencies))


