(ns mini.collections
  (:require [clojure.string :as str]))

(->> [1 2 3 4 5 6 7 8 9 10]
     (filter odd?)
     (map #(* % %))
     (str/join " - "))

;; (1 2 3 4 5) ;; Error since 1 is not a function

'(1 2 3 4 5) ;; ' or (quote (1 2 3 4 5)) means im referecing the value of the list

(cons 5 '(1 2 3 4)) ;; Adds in the beggining
(def valuess '(1 2 3 4))
(cons 5 valuess)

;; Clojure does not expect the first element of a vector to be a function 
(def my-vector [1 2 3])
(get my-vector 1);; 2
(my-vector 1) ;; 2
;;(my-vector 31) ;; IndexOutOfBoundsException
(get my-vector 31) ;; nil - get is null safety
(flatten [[1] [2 3] [4 5 [6 7]]]) ;; flatMap

(def my-map {:mina "cat", :red "squirrel", :taco "chihuahua"})

(first my-map) ;; Returns a MapEntry

(def odd-squares (comp (map #(* % %)) (filter odd?))) ;; comp creates a function that combine the functions provided as arguments
(into [] odd-squares (range 21))
