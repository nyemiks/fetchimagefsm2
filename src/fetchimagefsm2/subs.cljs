(ns fetchimagefsm2.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))



(re-frame/reg-sub
 ::get-image
 (fn [db]
   (:image db)))
