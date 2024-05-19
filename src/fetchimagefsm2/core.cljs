(ns fetchimagefsm2.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [fetchimagefsm2.events :as events]
   [fetchimagefsm2.views :as views]
   [fetchimagefsm2.config :as config]
   [day8.re-frame.http-fx]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
   ; (rdom/render [views/main-panel] root-el))
    (rdom/render [views/image-fetcher] root-el))
  )

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
