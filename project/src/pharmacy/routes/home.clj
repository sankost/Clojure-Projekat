(ns pharmacy.routes.home
  (:require [compojure.core :refer :all]
            [pharmacy.view.layout :as layout]
            [pharmacy.models.db :as db]            
            [clojure.string :as str]
            [hiccup.form :refer :all]
            [hiccup.core :refer [h]]
            [hiccup.page :as page]            
            [ring.util.response :as ring]))

(defn convert-answer [answer]
  (if (= answer true) "yes" "no"))

(defn show-all []
  [:table {:class "table-main"}
   [:thead
    [:tr
     [:th "Name"]
     [:th "Type"]
     [:th "Price"]
     [:th "Quantity"]
     [:th "Available"]]]
   (into [:tbody]
         (for [medicine (db/read-table)]
           [:tr
            [:td {:style "font-style: italic"}(:name medicine)]
            [:td (:type medicine)]
            [:td (:price medicine) "  RSD"]
            [:td (:quantity medicine)]
            [:td (convert-answer(:available medicine))]
            [:td {:style "border-bottom: 0px"} [:a {:class "button-add" :href (str "/details/" (h (:id medicine)))} "Details"]]]))])

(defn indexpage []
  (layout/common
    [:h1 {:class "title"}
     " - Pharmacy Online - "]
    [:h2 {:class "title-second"}
     "List of medicines:"]
    [:a {:href "/add" :class "button-add move-right" :style "float: left"} "Add new"]
    [:div {:class "search"}
    (form-to {:id "frm_insert" :class "register" :method "GET"}
         [:get "/search"]
         (text-field "search")"&nbsp&nbsp"
         (submit-button "Search")"&nbsp&nbsp"
         [:a {:href "/" :class "button-add"} "Reset"]
    )]
    [:br]
    [:br]
    (show-all)))


 (defn insert_or_update [& [id name type price quantity available]]
  (layout/common
  [:h2 {:class "details-title"} (if (nil? id) "Add new medicine:" "Update medicine:")]
  (form-to {:id "frm"}
    [:post "/save"]
    
     [:table {:class "table-details"} 
      [:tr {:style "display: none"}
         [:td "Id"]
         [:td (if (not (nil? id))
               (text-field {:readonly true :class "text-form"} "id" id))]
       ]
       [:tr
         [:td "Name"]
         [:td (if (not (nil? id))
               (text-field {:readonly true :class "text-form"} "name" name)
               (text-field {:class "text-form"} "name" name))]
       ]
       [:tr
         [:td "Type"]
         [:td (if (not (nil? id))
               (text-field {:readonly true :class "text-form"} "type" type)
               (text-field {:class "text-form"} "type" type))]
       ]
       [:tr
         [:td "Price"]
         [:td (text-field {:id "price" :class "text-form"} "price" price)]
       ]
       [:tr
         [:td "Quantity"]
         [:td (text-field {:class "text-form"} "quantity" quantity)]
       ]
       [:tr
         [:td "Available"]
         [:td (check-box {:class "text-form"} "available" available)]
       ]
      ]
           (submit-button {:onclick "return validate()"} (if (nil? id)"Save" "Update"))
           "&nbsp&nbsp"
           (if (not (nil? id))
           [:td [:a {:class "button-add" :href (str "/delete/"id)} "Delete"]])
           [:hr])
    [:a {:href "/" :class "button-add move-right" :style "float: left"} "Back"]
    ))

(defn show-medicine [medicine]
  (insert_or_update (:id medicine) (:name medicine) (:type medicine) (:price medicine) (:quantity medicine) (:available medicine)))

(defn convert-boolean [arg]
  (if (= arg "true") 1 0))

(defn save [name type price quantity available &[id]]
    (if (not (nil? id))
      (db/update id price quantity (convert-boolean available))
      (db/save name type price quantity (convert-boolean available)))
  (ring/redirect "/"))

(defn delete [id]
    (if (not (= id 0))
      (db/delete id))
  (ring/redirect "/"))

(defroutes home-routes
  (GET "/" [](indexpage))
  (GET "/details/:id" [id](show-medicine (db/find id)))
  (GET "/add" [](insert_or_update))
  (POST "/save" [name type price quantity available id](save name type price quantity available id))
  (GET "/delete/:id" [id](delete id))
  ;(GET "/search" [](search))
  )
