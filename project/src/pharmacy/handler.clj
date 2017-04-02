(ns pharmacy.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [pharmacy.models.db :as db]
            [pharmacy.routes.home :refer [home-routes]]))

(defn init []
  (if-not (.exists (java.io.File. "./db.sql"))
    (db/create-table)))

(defn destroy []
  (println "Shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Page not found!"))

(def app
  (-> (routes home-routes app-routes)
      (handler/site)
      (wrap-base-url)))