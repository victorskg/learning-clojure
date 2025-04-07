(ns mini.threding)

;; Thread first
(defn hire
  [employee]
  (let [email (str (:first-name employee) "@nubank.com.br")
        employee-with-email (assoc employee :email email)
        employee-with-history (update employee-with-email :employment-history conj "Nubank")]
    (assoc employee-with-history :hired-at (java.util.Date.))))

(defn hire-2 [employee]
  (->
   employee
   (assoc :email (str (:first-name employee) "@nubank.com"))
   (update :employment-history conj "Nubank")
   (assoc :hired-at (java.util.Date.)))) ;; thread-first - the first value is put in the first argument of the next function

(hire {:first-name "João"})
(hire-2 {:first-name "João"})

;; Thread last
(def employees
  [{:first-name "A" :last-name "B" :email "A@nubank.com.br" :hired-at #inst "2022-05-20"}
   {:first-name "C" :last-name "D" :email "C@nubank.com.br" :hired-at #inst "2022-05-21"}
   {:first-name "E" :last-name "F" :email "E@nubank.com.br" :hired-at #inst "2022-05-21"}
   {:first-name "G" :last-name "H" :email "G.H@nubank.com.br" :hired-at #inst "2022-05-21"}
   {:first-name "I" :last-name "J" :email "I.J@nubank.com.br" :hired-at #inst "2022-05-22"}])

(defn email-address
  "Retorna um endereço de email formatado com :first-name e :last-name"
  [employee]
  (format "%s.%s@nubank.com.br"
          (:first-name employee)
          (:last-name employee)))

(defn old-email-format?
  "Retorna true caso o endereço de email esteja no antigo formato"
  [employee]
  (not= (:email employee) (email-address employee)))

(defn update-email
  "Atualiza o endereço de email do funcionario para o novo formato"
  [employee]
  (assoc employee :email (email-address employee)))

(defn hired-day
  "Retorna o dia de contratação em :hired-at."
  [employee]
  (.getDay (:hired-at employee)))

(defn employees-with-old-email-format []
  (->> employees
       (filter old-email-format?)
       (map update-email)
       (group-by hired-day)))
(employees-with-old-email-format)

;;(-> {:first-name "Rich" :last-name "Hickey"}
;;    :hired-at
;;    .getTime) ;; Erro de execução (NullPointerException)

(some-> {:first-name "Rich" :last-name "Hickey"}
        :hired-at
        .getTime) ;; => nil some-> = Optional/Null safety

(defn describe-number
  [n]
  (cond-> []
    (odd? n)  (conj "odd")
    (even? n) (conj "even")
    (zero? n) (conj "zero")
    (pos? n)  (conj "positive"))) ;; passes to the first argument

(describe-number 3) ;; => ["odd" "positive"]
(describe-number 4) ;; => ["even" "positive"]

(as-> {:ints (range 5)} $ ;; creates a symbol to be used in any order - can be any symbol
  (:ints $)
  (map inc $)  ;; last position
  (conj $ 10)  ;; first position
  (apply + $)) ;; => 25

(defn hire-3 [employee]
  (as->
   employee $
    (assoc $ :email (str (:first-name employee) "@nubank.com"))
    (update $ :employment-history conj "Nubank")
    (assoc $  :hired-at (java.util.Date.)))) ;; thread-first - the first value is put in the first argument of the next function

(hire-3 {:first-name "João"})
