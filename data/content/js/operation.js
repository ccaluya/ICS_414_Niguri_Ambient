google.load("feeds", "1");

var settingsFile = "settings.txt";
var defaultSettings = "Set RSS Feed = None\nUpdate Hours = 0\nUpdate Minutes = 0\nUpdate Seconds = 0\nMax Notifications = 1\nLast Update At = None\nLast RSS Post= None\nUpdate Interval (Milliseconds) = 0";
var setupSettings = setTimeout(loadSettings, 1000);
var suspendUpdate = false;
var rssSettings =
{
  rssFeed: "None",
  updateHours: 0,
  updateMinutes: 0,
  updateSeconds: 10,
  maxNotifications: 1,
  lastUpdate: "None",
  lastPost: "None",
  updateInterval: 0
};

function resetSettings(){
  var fs3 = require("fs");
  var writeTo = fs3.createWriteStream(settingsFile);
  writeTo.write(defaultSettings);
  writeTo.end();

  /* Reset Values in rssSettings Array */
  rssSettings.rssFeed = "None";
  rssSettings.updateHours = 0;
  rssSettings.updateMinutes = 0;
  rssSettings.updateSeconds = 0;
  rssSettings.maxNotifications = 1;
  rssSettings.lastUpdate = "None";
  rssSettings.lastPost = "None";
  rssSettings.updateInterval = 0;
  alert("Reset Settings");

  checkUpdates();
}

function saveSettings(form){
  var setRssFeed = form.rssUrl.value;
  var setUpdateHours = form.uHours.value;
  var setUpdateMinutes = form.uMinutes.value;
  var setUpdateSeconds = form.uSeconds.value;
  var setMaxNotifications = form.mxnot.value;
  /*var setLastUpdate = form.lastUpdateTime.value;
  var setLastPost = form.lastRSSPost.value;
  var setUpdateInterval = calcMs(setUpdateHours, setUpdateMinutes, setUpdateSeconds);*/

  var changedSettings = "";
  var srssFeed = "Set RSS Feed = ";
  var supdateHours = "Update Hours = ";
  var supdateMinutes = "Update Minutes = ";
  var supdateSeconds = "Update Seconds = ";
  var smaxNotif = "Max Notifications = ";
  var slastUpdate = "Last Update At = ";
  var slastPost = "Last RSS Post = ";
  var supdateInterval = "Update Interval (Milliseconds) = "

  var fs4 = require("fs");
  var writeTo = fs4.createWriteStream(settingsFile);

  changedSettings += srssFeed.concat(setRssFeed + "\n");
  changedSettings += supdateHours.concat(setUpdateHours + "\n");
  changedSettings += supdateMinutes.concat(setUpdateMinutes + "\n");
  changedSettings += supdateSeconds.concat(setUpdateSeconds+ "\n");
  changedSettings += smaxNotif.concat(setMaxNotifications + "\n");
  /*changedSettings += slastUpdate.concat(setLastUpdate + "\n");
  changedSettings += slastPost.concat(setLastPost + "\n");
  changedSettings += supdateInterval.concat(setUpdateInterval + "\n");*/

  /* Update settings.txt document */
  writeTo.write(changedSettings);
  writeTo.end();

  /* Update Values in rssSettings Array */
  rssSettings.rssFeed = setRssFeed;
  rssSettings.updateHours = setUpdateHours;
  rssSettings.updateMinutes = setUpdateMinutes;
  rssSettings.updateSeconds = setUpdateSeconds;
  rssSettings.maxNotifications = setMaxNotifications;
  /*rssSettings.lastUpdate = setLastUpdate;
  rssSettings.lastPost = setLastPost;*/

  alert("Settings Saved");
}

function checkUpdates(){
  if(rssSettings.lastPost == "None"){
    document.getElementById("settingsArea").style.display = "block";
    document.getElementById("active").style.display = "none";
    document.getElementById("pending").style.display = "block";
  }
  else{
    document.getElementById("settingsArea").style.display = "none";
    document.getElementById("pending").style.display = "none";
    document.getElementById("active").style.display = "block";
  }
}

function setSettings(setup){
  var settingsOptions = setup.split("\n");
  var currentOption = {};
  var currentItem = "";
  var settingForm = document.forms[0];
  checkUpdates();

  /*for(i = 0; i < settingsOptions.length; i++){
    currentOption = settingsOptions[i].split("=");
    currentItem = currentOption[1];

    if(i = 0){

    }
    else{
      break;
    }
  }*/
  clearTimeout(setupSettings);
}

function loadSettings(){
  var fs1 = require("fs");

  fs1.exists(settingsFile, function(exists){
    if(!exists){
      fs1.appendFile(settingsFile, defaultSettings, function(err){
        if(err){
          alert(err.toString());
        }
      });
    }
    else{
      var loadSet = fs1.createReadStream(settingsFile);
      var setup = "";

      /*loadSet.on('error', function(err){
        alert(settingsFile + "couldn't be read.");
      });*/

      loadSet.on('data', function(data) {
        setup+=data;
      });

      loadSet.on('end', function() {
        setSettings(setup);
      });
    }
  });
  fs1.end();
}

function calcMS(hours, minutes, seconds){
  var ms = 0;
  var h = hours * 3600000;
  var m = minutes * 60000;
  var s = seconds * 1000;

  ms = h+m+s;

  return ms;
}

function launch(form){

  saveSettings(form);
  var feed = new google.feeds.Feed(rssSettings.rssFeed);
  feed.setNumEntries(rssSettings.maxNotifications);

  feed.load(function(result) {
    if (!result.error) {
      var container = document.getElementById("feed");
      for (var i = 0; i < result.feed.entries.length; i++) {
        var entry = result.feed.entries[i];
        rssSettings.lastPost = entry.title;
        alert(rssSettings.lastPost);
        checkUpdates();
      }
    }
    else{
      resetSettings();
      alert("Invalid RSS Url.");
    }
  });
}

