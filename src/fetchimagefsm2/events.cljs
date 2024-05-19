(ns fetchimagefsm2.events
  (:require
   [re-frame.core :as re-frame]
   [fetchimagefsm2.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [ajax.core :as ajax]
    [ajax.protocols :refer [-body]]
   ))


(def image-url "https://picsum.photos/200/300")

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))


(re-frame/reg-event-db
  ::update-image
  (fn [db [_ url]]
    (.info js/console "update image url: " url)
    (assoc db :image url)
    ; db
    )
 )


(re-frame/reg-event-fx                             ;; note the trailing -fx
  ::handler-with-http                      ;; usage:  (dispatch [:handler-with-http])
  (fn [{:keys [db]} _]                    ;; the first param will be "world"
   
   (.info js/console "get image from url: " image-url)
    
    {:db   (assoc db :show-twirly false)   ;; causes the twirly-waiting-dialog to show??
     :http-xhrio {:method          :get
                  :uri             image-url
                  :timeout         8000                                           ;; optional see API docs
                  :response-format {
                                     :content-type "image/png"
                                     :type :blob
                                     :description "PNG file"
                                     :read -body
                                    }  ;; IMPORTANT!: You must provide this.
                  :on-success      [::success-http-result]
                  :on-failure      [::failure-http-result]}}))



(re-frame/reg-event-db
  ::success-http-result
  (fn [db [_ result]]
    (.info js/console "success result: " result)
    (let [
           url (.createObjectURL js/URL result)
         ]
          (.info js/console "success result: " url)
          (assoc db :image url)
      )
    )
 )


(re-frame/reg-event-db
  ::failure-http-result
  (fn [db [_ result]]
    ;; result is a map containing details of the failure
    (assoc db :failure-http-result result)))