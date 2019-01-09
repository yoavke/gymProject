function retrieve() {

    //set the footer
    let my_foot = document.querySelectorAll(".ui-footer.ui-bar-inherit");
    for (let i=0;i<my_foot.length;i++) {
        my_foot[i].innerHTML = '<a href="index.html">Go Home</a> HIT Project - Yoav Keren';
    }

    //return current page
    let pathname = window.location.pathname;
    let page = pathname.slice(15);

    if (page == 'browseAerobic.html') {
        browseAerobic();
    }
    else if (page=='browseAnaerobic.html') {
        browseAnaerobic();
    }
    else if (page=="details.html") {
        details();
    }
    else if(page=='addAerobic.html') {
        addAerobic();
    }
    else if (page == 'addAnAerobic.html') {
        addAnAerobic();
    }
    else if (page=='myGraphs.html') {
        myGraphs();
    }
}

//browse Aerobic activities
function browseAerobic() {
    let json;
    let activities;

    const urlParams = new URLSearchParams(window.location.search);
    let startDate = urlParams.get('start');
    let endDate = urlParams.get('end');

    //set date picker start and end dates
    $(function() {
        $("#datePickerStart").datepicker({
            onSelect: function(startDate) {
                let parseDate = new Date(startDate);
                let minYear = parseDate.getFullYear();
                let minMonth = parseDate.getMonth();
                let minDay = parseDate.getDate();
                let minDate = new Date(minYear,minMonth,minDay,0,0,0,0);
                minDate.setDate(minDate.getDate()+1);
                $("#datePickerEnd").datepicker("option","minDate",minDate);
                $("#datePickerEnd").datepicker("refresh");
                $("#datePickerEnd").val("");
            }
        })

        $("#datePickerEnd").datepicker();

        let filter = document.querySelector("#filter");

        filter.addEventListener("click",function() {
            showAerobic($("#datePickerStart").val(),$("#datePickerEnd").val());
        })
    });

    showAerobic(startDate,endDate);
}

function showAerobic(startDate,endDate){
    //send 1 to browseActivites() method to retrieve only Aerobic activities
    let startDay=null, startMonth=null, startYear=null;
    let endDay=null, endMonth=null, endYear=null;

    if (startDate!=null && startDate!="") {
        startDay = startDate.slice(3,5);
        startMonth = startDate.slice(0,2);
        startYear = startDate.slice(6,10);
    }

    if (endDate!=null && endDate!="") {
        endDay = endDate.slice(3,5);
        endMonth = endDate.slice(0,2);
        endYear = endDate.slice(6,10);
    }

    console.log("console log: start- "+startDate+" end- "+endDate);

    json = JSON.parse(window.browseFromDb.browseActivities("1",(startDate!=null && startDate!="")?(startDay+"/"+startMonth+"/"+startYear).toString():null,(endDate!=null && endDate!="")?(endDay+"/"+endMonth+"/"+endYear).toString():null));
    activities = json.activities;

    //reset div content
    document.querySelector("#activityList").innerHTML = "";

    for (let i = 0 ; i<activities.length;i++) {
        let date = new Date(parseInt(activities[i].timestamp));
        document.querySelector("#activityList").innerHTML += "<div data-role='collapsible' class='coll'>";
        document.querySelector("#activityList").innerHTML += "<h3>"+(i+1)+". "+activities[i].activity+ " (" + date.getDate() +"/"+ (parseInt(date.getMonth())+1) +"/"+ date.getFullYear() + ")</h3>";
        document.querySelector("#activityList").innerHTML += activities[i].length + " KM" + " <a href='details.html?activityId="+activities[i]._id+"'>details</a></p>";
        document.querySelector("#activityList").innerHTML += "</div>";
    }
    //$( "#activityList" ).collapsibleset( "refresh" );
}

//browse Anaerobic activities
function browseAnaerobic() {
    let json;
    let activities;

    const urlParams = new URLSearchParams(window.location.search);
    let startDate = urlParams.get('start');
    let endDate = urlParams.get('end');

    //set date picker start and end dates
    $(function() {
        $("#datePickerStart").datepicker({
            onSelect: function(startDate) {
                let parseDate = new Date(startDate);
                let minYear = parseDate.getFullYear();
                let minMonth = parseDate.getMonth();
                let minDay = parseDate.getDate();
                let minDate = new Date(minYear,minMonth,minDay,0,0,0,0);
                minDate.setDate(minDate.getDate()+1);
                $("#datePickerEnd").datepicker("option","minDate",minDate);
                $("#datePickerEnd").datepicker("refresh");
                $("#datePickerEnd").val("");
            }
        })

        $("#datePickerEnd").datepicker();

        let filter = document.querySelector("#filter");

        filter.addEventListener("click",function() {
            showAnAerobic($("#datePickerStart").val(),$("#datePickerEnd").val());
        })
    });

    showAnAerobic(startDate,endDate);
}

function showAnAerobic(startDate,endDate){
    //send 1 to browseActivites() method to retrieve only Aerobic activities
    let startDay=null, startMonth=null, startYear=null;
    let endDay=null, endMonth=null, endYear=null;

    if (startDate!=null && startDate!="") {
        startDay = startDate.slice(3,5);
        startMonth = startDate.slice(0,2);
        startYear = startDate.slice(6,10);
    }

    if (endDate!=null && endDate!="") {
        endDay = endDate.slice(3,5);
        endMonth = endDate.slice(0,2);
        endYear = endDate.slice(6,10);
    }

    console.log("console log: start- "+startDate+" end- "+endDate);

    json = JSON.parse(window.browseFromDb.browseActivities("2",(startDate!=null && startDate!="")?(startDay+"/"+startMonth+"/"+startYear).toString():null,(endDate!=null && endDate!="")?(endDay+"/"+endMonth+"/"+endYear).toString():null));
    activities = json.activities;

    //reset div content
    document.querySelector("#activityList").innerHTML = "";

    for (let i = 0 ; i<activities.length;i++) {
        let date = new Date(parseInt(activities[i].timestamp));
        document.querySelector("#activityList").innerHTML += "<div data-role='collapsible' class='coll'>";
        document.querySelector("#activityList").innerHTML += "<h3>"+(i+1)+". "+activities[i].activity+ " (" + date.getDate() +"/"+ (parseInt(date.getMonth())+1) +"/"+ date.getFullYear() + ")</h3>";
        document.querySelector("#activityList").innerHTML += activities[i].length + " Min" + " <a href='details.html?activityId="+activities[i]._id+"'>details</a></p>";
        document.querySelector("#activityList").innerHTML += "</div>";
    }
    //$( "#activityList" ).collapsibleset( "refresh" );
}

//show details about a particular activity
function details() {
    let json;
    let activities;

    const urlParams = new URLSearchParams(window.location.search);
    const activityId = parseInt(urlParams.get('activityId'));

    json = JSON.parse(window.detailsFromDb.selectDetails(activityId));
    activities = json.activities;
    let date = new Date(parseInt(activities[0].timestamp));
    document.querySelector("#details").innerHTML += "<h2>" +activities[0].activity+ " (" + date.getDate() +"/"+ (parseInt(date.getMonth())+1) +"/"+ date.getFullYear() + ")</h2>";
    document.querySelector("#details").innerHTML += "<p>"+activities[0].length + " " + (activities[0].activity=='swimming'?"pools":"km") + "</p>";
    document.querySelector("#deleteActivity").addEventListener("click",function() {
        window.deleteActivityFromDb.deleteActivity(parseInt(activities[0]._id));
    })
}

//add a new aerobic activity to the database
function addAerobic() {
    let addAerobicActivity = document.querySelector("#addAeActivity");

    //interact with the JAVA code of the application
    //addToDb property is the string we send from the JAVA code
    //addActivity is the method in the object of the JAVA code
    addAerobicActivity.addEventListener("click",function() {
        window.addToDb.addActivity(document.querySelector("#AerobicActivity").value,document.querySelector("#lengthKm").value);
    });
}

//add a new anaerobic activity to the database
function addAnAerobic() {
    let addAnAerobicActivity = document.querySelector("#addAnActivity");

    //interact with the JAVA code of the application
    //addToDb property is the string we send from the JAVA code
    //addActivity is the method in the object of the JAVA code
    addAnAerobicActivity.addEventListener("click",function() {
        window.addToDb.addActivity(document.querySelector("#AnaerobicActivity").value,document.querySelector("#lengthMinutes").value);
    });
}

//show stats about the activities in the past week (starts on Sunday)
function myGraphs() {
    //retrieve aerobic data
    let jsonAerobic = JSON.parse(window.kmFromDb.selectKm(1));
    let activitiesAerobic = jsonAerobic.activities;

    //retrieve anaerobic data
    let jsonAnAerobic = JSON.parse(window.kmFromDb.selectKm(2));
    let activitiesAnAerobic = jsonAnAerobic.activities;

    //aerobic chart raw object
    let obj = {
        bindto: '#chart',
        data: {
            columns: [

            ],
            type : 'donut'
        },
        donut: {
            title: "Aerobic",
            label: {
                format: function(value, ratio, id) {
                    return value+"km";
                }
            }
        }
    };

    //anaerobic chart raw object
    let obj2 = {
        bindto: '#chartAn',
        data: {
            columns: [

            ],
            type : 'donut'
        },
        donut: {
            title: "Anaerobic",
            label: {
                format: function(value, ratio, id) {
                    return value+"min";
                }
            }
        }
    };

    //set columns in the aerobic chart
    for (let i=0;i<activitiesAerobic.length;i++)
    {
        let arr = [activitiesAerobic[i].activity, activitiesAerobic[i].km];
        obj.data.columns.push(arr);
    }

    //set columns in the anaerobic chart
    for (let i = 0; i < activitiesAnAerobic.length; i++) {
        let arr = [activitiesAnAerobic[i].activity, activitiesAnAerobic[i].minutes];
        obj2.data.columns.push(arr);
    }

    console.log("chart aerobic: " + obj.bindto);
    console.log("chart anaerobic: " + obj2.bindto);

    //show chart or error msg if no aerobic activities this week
    if (activitiesAerobic.length==0) {
        console.log("No aerobic activities this week");
        document.querySelector("#chart").innerHTML = "<b>No aerobic activities this week</b>";
    } else {
        var chart = c3.generate(obj);
    }

    //show chart or error msg if no anaerobic activities this week
    if (activitiesAnAerobic.length==0) {
        console.log("No anaerobic activities this week");
        document.querySelector("#chartAn").innerHTML = "<b>No anaerobic activities this week</b>";

    } else {
        var chart2 = c3.generate(obj2);
    }

    //get the size of the window and set it to be the width of the chart
    setTimeout(function () {
        let pageWidth = getWidth() - 40;
        chart.resize({width:pageWidth});
        chart2.resize({width:pageWidth});
    }, 100);
}

//get the width of the screen
function getWidth() {
    return Math.max(
        document.body.scrollWidth,
        document.documentElement.scrollWidth,
        document.body.offsetWidth,
        document.documentElement.offsetWidth,
        document.documentElement.clientWidth
    );
}