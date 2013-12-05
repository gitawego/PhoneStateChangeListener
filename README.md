PhoneStateChangeListener
========================
**developed under cordova 3.x and tested with Android Api level 17**

Cordova plugin to provide callback when Telephony state changes

Possible states are IDLE, OFFHOOK and RINGING

##how to intall
use cordova cli to install this plugin:
```
cordova plugin add https://github.com/gitawego/PhoneStateChangeListener.git
```
##usage
start to watch the statechange
```js
navigator.phoneStateChangeListener.watch(function(err){
    if(err){
        throw new Error('something goes wrong while activating the BroadcastReceiver');
    }
});
```
then use it as a normal event on document
```js
document.addEventListener('phonestatechange',function(evt){
    console.log(evt.state);
    console.log(evt.number);
    console.log(evt.state === evt.STATES.RINGIGNG);
},false);
```
stop the watcher
```js
navigator.phoneStateChangeListener.unwatch(function(err){
    if(err){
        throw new Error('something goes wrong while deactivating the BroadcastReceiver');
    }
});
```