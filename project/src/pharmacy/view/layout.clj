(ns pharmacy.view.layout
  (:require [hiccup.page :refer :all]))

(defn common [& body]
  (html5
    [:head
     [:title "Pharmacy online"]
     (include-css "/css/project.css")
     (include-js "/js/validation.js")]
    [:body 
     [:div {:class "container"}
     body
     ]
     ]))
