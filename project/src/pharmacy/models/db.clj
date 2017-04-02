(ns pharmacy.models.db
  (:require [clojure.java.jdbc :as sql]))

	(let [db-host "localhost"
	      db-port 3306
	      db-name "db"]
 
  (def db {:classname "com.mysql.jdbc.Driver" 
           :subprotocol "mysql"
           :subname (str "//" db-host ":" db-port "/" db-name)
           :user "root"
           :password ""}))

(defn read-table []
  (sql/with-connection
    db
    (sql/with-query-results res
      ["SELECT * FROM pharmacy ORDER BY id ASC"]
      (doall res))))


(defn save [name type price quantity available]
  (sql/with-connection
    db
    (sql/insert-values
      :pharmacy
      [:name :type :price :quantity :available]
      [name type price quantity available])))

(defn find [id]
  (first
    (sql/with-connection
      db
      (sql/with-query-results res
        ["SELECT * FROM pharmacy WHERE id= ?" id]
        (doall res)))))

(defn delete [id]
  (sql/with-connection
    db
    (sql/delete-rows
      :pharmacy
      ["id=?" id])))

(defn update [id price quantity available]
  (sql/with-connection
    db
    (sql/update-values
      :pharmacy
      ["id=?" id]
      {:price price :quantity quantity :available available})))

(defn search [string]
  (first
    (sql/with-connection
      db
      (sql/with-query-results res
        ["SELECT * FROM pharmacy WHERE name CONTAINS ?" string]
        (doall res)))))