(ns fetchimagefsm2.views
  (:require
   [re-frame.core :as re-frame]
   [fetchimagefsm2.subs :as subs]
   [maximgb.re-state.core :as rs]
   [fetchimagefsm2.events :as events]
   ;[fetchimagefsm2.subs :as subs]
  
   ))


;(defonce image-url "https://picsum.photos/200/300")

 ;( def image        (r/atom nil) )

       


(def image-url "https://picsum.photos/200/300")


(defn fetch-image []
   (.info js/console "fetch image from: " image-url)
  
(-> (js/fetch image-url)
; (. then (fn [response] (.json response)))
    (. then (fn [response] (.blob response)))
(. then (fn [data]
          (let [
                url (.createObjectURL js/URL data)
                ]
            (.info js/console "image json response: " url)        
           ;  (reset! image url )
            ; (reset! isSuccess true)
             ;(rs/interpreter-send! controller :getimage)
              (re-frame/dispatch [::events/update-image url])    
            )
          )
   )
(. catch (fn [e]
           (.info js/console  "error trace: " e)
           ; (reset! isError true)
           )
   )
    (. finally (fn []
                  (.info js/console "finally !! fetching set to false ")
           ; (reset! isFetching false)
           )
   )
    )
  )


;(def imageFetcher "imagefetch")



(rs/def-machine {
  :id :imageFetcher,
  :initial :ready,
  :context {
    :image nil
  },
  :states {
    :ready {
      :on {
        :BUTTON_CLICKED :fetching
      }
    },
    :fetching {
               :entry  (fn [context & _]
                          (.info js/console "context: " context) 
                         (.info js/console "fetch image...") 
                            ; (fetch-image)
                           (re-frame/dispatch [::events/handler-with-http])
                         )
               :on
                   {:imagefetched {:target :success
                               :actions (fn [& _]
                                     (.info js/console "image fetched ! ")
                                          )
                              }
					   }
			}
    },
    :success {},
    :error {}
  }
)



 ;  (def controller (rs/interpreter-start! (rs/interpreter! imageFetcher)))
 ;  (def state-sub (rs/isubscribe-state controller))




(defn image-fetcher []
  (let 
       [
           controller (rs/interpreter-start! (rs/interpreter! imageFetcher))
           state-sub (rs/isubscribe-state controller)
           image      @(re-frame/subscribe [::subs/get-image])
       ]
    (.info js/console "render image fetcher. curent state: " @state-sub)
    [:section
     (when (= @state-sub "fetching") [:p "loading..."])
     (when (= @state-sub "success")  [:div [:img {
                                    :src image
                                    :alt ""
                                    :style {
                                            :height "150px"
                                            :width "150px"
                                            :border "solid gray 3px" 
                                            :border-radius "10px"
                                            }
                                    }
                              ]
                        ]
           )
     (when (= @state-sub "error") [:p "An error occured"])
     [:button {
               :on-click (fn []
                          ; (fetch-image controller)  
                           (.info js/console "button clicked ! fetch image ..")
                              (rs/interpreter-send! controller :BUTTON_CLICKED)
                           )
               }"Get Image"]
     ])
  )



(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1
      "Hello from " @name]
     ]
    )
  )
