(ns ten-or-ninety-four.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [clj-http.client :as client]
            [cheshire.core :refer :all]
            [ring.middleware.json :as middleware]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))


(defn get-directions
  [from to]
  (parse-string
    (:body
      (clj-http.client/get
        "https://maps.googleapis.com/maps/api/directions/json"
        {:query-params {"origin" from
                        "destination" to
                        "key" (System/getenv "GOOGLE_API_KEY")}}))
    true))


(defn duration
  [route]
  (reduce #(+ %1 (:value (:duration %2)))
          0
          (:legs route)))


(defn seconds->time-string
  [seconds]
    (str (int (Math/ceil (/ seconds 60))) "m"))


(defn sort-routes-by-duration
  [routes]
  (sort-by duration routes))


(defn goog-route->route-obj
  [route]
  {:duration-raw (duration route)
   :duration (seconds->time-string (duration route))
   :summary (:summary route)})


(defn handle-get-directions
  [from to]
  (let [routes (:routes (get-directions from to))]
    {:body (map goog-route->route-obj (sort-routes-by-duration routes))
     :headers {"Content-Type" "application/json"}}))


(defroutes app-routes
  (GET "/" [] "/directions/:from/:to")

  (GET "/directions/:from/:to" [from to]
       (handle-get-directions from to))

  (route/not-found "404 Not Found"))


(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))

