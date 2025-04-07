(ns mini.atom)

;; Atoms

(def first-atom (atom 0))
(println (deref first-atom)) ;; should deref the atom to get his value
(println @first-atom) ;; this is also a deref

(swap! first-atom + 5) ;; swap! to change an atom - should provide a function to do that
(swap! first-atom (fn [v] (+ v 10))) ;; another example - this changes always considers the previous value - because of that, the function must have one argument (prev value)

(reset! first-atom 1) ;; if u want to change the value ignoring the previous one, use reset!
(println @first-atom)

;; Refs
(def first-ref (ref ["red" "blue"]))
(println @first-ref)
(conj @first-ref "purple")

(def other-ref (ref ["black" "white"]))
(dosync
 (let [moving (last @other-ref)]
   (alter first-ref conj moving)
   (alter other-ref pop))) ;; dosync is used when working with multiple threads modifying the same ref at the same time - similar to a locking operation in rds
(println first-ref "--" other-ref)
