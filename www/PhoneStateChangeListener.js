/**
 *  PhoneStateChangeListener cordova plugin (Android)
 * 
 *  @author Stéfano Stypulkowski Zanata
 *  @see http://szanata.com
 *  @reference https://github.com/madeinstefano/PhoneStateChangeListener
 *  @license MIT <http://szanata.com/MIT.txt>
 *  @license GNU <http://szanata.com/GNU.txt>
 *  
 *  Based upon PhoneListener by authored by Tommy-Carlos Williams <https://github.com/devgeeks>
 * 
 */
var exec = require('cordova/exec');
var buildEvent = function(evtName,params){
    var eventObj = document.createEventObject ?
            document.createEventObject() : document.createEvent("Events"),
        params = params || {};

    if (eventObj.initEvent) {
        eventObj.initEvent(evtName,
            ('canBubble' in params) ? params.canBubble : true,
            ('cancelable' in params) ? params.cancelable : true);
    }
    Object.keys(params).forEach(function (name) {
        eventObj[name] = params[name];
    });
    if ('keyCode' in params) {
        eventObj.which = params.keyCode;
    }
    return eventObj;
};
var fireEvent = function (evtName, params) {
    var eventObj = buildEvent(evtName, params),
        el = params.el || document;
    return el.dispatchEvent ? el.dispatchEvent(eventObj) : el.fireEvent("on" + evtName, eventObj);
}
module.exports = {
  states:{
    RINGIGNG: 'RINGING',
    OFFHOOK: 'OFFHOOK',
    IDLE: 'IDLE',
    NONE: 'NONE'
  },
  watch:function(callback){
    var self = this;
    exec(function (data){
      fireEvent('phonestatechange',{
        el:document,
        STATES:self.states,
        state:data.state,
        number:data.number
      });
    },function (error){
      callback && callback(error);
    },'PhoneStateChangeListener','start',[]);
  },
  unwatch:function(callback){
    exec(callback, callback, 'PhoneStateChangeListener', 'stop',[]);
  }
};
