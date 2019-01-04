function loginQueryString()
{
    //get the querystring
    const urlParams = new URLSearchParams(window.location.search);
    const myParam = urlParams.get('action');

    // login.html?action=logout - disconnect user from the app
    if (myParam === 'logout')
    {
        //TODO: delete cookie and transfer to login with no query string
    }
}

function authentication()
{
    //cookie of the user
    let userCookie = document.cookie;

    //page to get transferred if not logged in yet
    let loginPage = "login.html";

    //if user is not identified - transfer to login page
    if (userCookie === "" || typeof userCookie !== "string")
    {
        //transfer to the login page
        location.assign(loginPage);

        //TODO: remove code below
        //console.log("Cookie is empty or not a string");
    }
}

window.onload = function ()
{

}

function retrieve() {

    //handle the footer
    let my_foot = document.querySelectorAll(".ui-footer.ui-bar-inherit");
    for (let i=0;i<my_foot.length;i++) {
        my_foot[i].innerHTML = '<a href="index.html">Go Home</a> HIT Project - Yoav Keren';
    }


    //return current page
    let pathname = window.location.pathname;
    let page = pathname.slice(15);

    var json;
    var activities;

    if (page == 'browseAerobic.html') {
        //send 1 to browseActivites() method to retrieve only Aerobic activities
        json = JSON.parse(window.browseFromDb.browseActivities("1"));
        activities = json.activities;
        for (let i = 0 ; i<activities.length;i++) {
            let date = new Date(parseInt(activities[i].timestamp));
            document.querySelector("#activityList").innerHTML += "<div data-role='collapsible' class='coll'>";
            document.querySelector("#activityList").innerHTML += "<h3>"+(i+1)+". "+activities[i].activity+ " (" + date.getDate() +"/"+ (parseInt(date.getMonth())+1) +"/"+ date.getFullYear() + ")</h3>";
            document.querySelector("#activityList").innerHTML += activities[i].length + " KM" + " <a href='details.html?activityId="+activities[i]._id+"'>details</a></p>";
            document.querySelector("#activityList").innerHTML += "</div>";
        }
        $( "#activityList" ).collapsibleset( "refresh" );
    }
    else if (page=='browseAnaerobic.html') {
        //send 2 to browseActivites() method to retrieve only Aerobic activities
        json = JSON.parse(window.browseFromDb.browseActivities("2"));
        activities = json.activities;
        for (let i = 0 ; i<activities.length;i++) {
            let date = new Date(parseInt(activities[i].timestamp));
            document.querySelector("#activityList").innerHTML += "<div data-role='collapsible' class='coll'>";
            document.querySelector("#activityList").innerHTML += "<h3>"+(i+1)+". "+activities[i].activity+ " (" + date.getDate() +"/"+ (parseInt(date.getMonth())+1) +"/"+ date.getFullYear() + ")</h3>";
            document.querySelector("#activityList").innerHTML += activities[i].length + " KM" + " <a href='details.html?activityId="+activities[i]._id+"'>details</a></p>";
            document.querySelector("#activityList").innerHTML += "</div>";
        }
        $( "#activityList" ).collapsibleset( "refresh" );
    }
    else if (page=="details.html") {
        const urlParams = new URLSearchParams(window.location.search);
        const activityId = parseInt(urlParams.get('activityId'));

        json = JSON.parse(window.detailsFromDb.selectDetails(activityId));
        activities = json.activities;
        let date = new Date(parseInt(activities[0].timestamp));
        document.querySelector("#details").innerHTML += "<h2>" +activities[0].activity+ " (" + date.getDate() +"/"+ (parseInt(date.getMonth())+1) +"/"+ date.getFullYear() + ")</h2>";
        document.querySelector("#details").innerHTML += "<p>"+activities[0].length + " " + (activities[0].activity=='swimming'?"pools":"km") + "edit</a> | delete</p>";
    }
    else if(page=='addAerobic.html') {

        let addAerobicActivity = document.querySelector("#addAeActivity");

        //interact with the JAVA code of the application
        //addToDb property is the string we send from the JAVA code
        //addActivity is the method in the object of the JAVA code
        addAerobicActivity.addEventListener("click",function() {
            window.addToDb.addActivity(document.querySelector("#AerobicActivity").value,document.querySelector("#lengthKm").value);
        });

    }
    else if (page == 'addAnAerobic.html') {
        let addAnAerobicActivity = document.querySelector("#addAnActivity");

        //interact with the JAVA code of the application
        //addToDb property is the string we send from the JAVA code
        //addActivity is the method in the object of the JAVA code
        addAnAerobicActivity.addEventListener("click",function() {
            window.addToDb.addActivity(document.querySelector("#AnaerobicActivity").value,document.querySelector("#lengthMinutes").value);
        });
    }
    else if (page=='myGraphs.html') {
        let jsonAerobic = JSON.parse(window.kmFromDb.selectKm(1));
        let activitiesAerobic = jsonAerobic.activities;

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
                        return value+"minutes";
                    }
                }
            }
        };

        for (let i=0;i<activitiesAerobic.length;i++)
        {
            let arr = [activitiesAerobic[i].activity, activitiesAerobic[i].km];
            console.log(arr);
            obj.data.columns.push(arr);
        }

        for (let i=0;i<activitiesAnAerobic.length;i++)
        {
            let arr = [activitiesAnAerobic[i].activity, activitiesAnAerobic[i].minutes];
            console.log("error");
            obj2.data.columns.push(arr);
        }

        //generate the chart
        let chart = c3.generate(obj);

        //generate the chart
        let chart2 = c3.generate(obj2);

        //get the size of the window and set it to be the width of the chart
        setTimeout(function () {
            let pageWidth = getWidth() - 40;
            chart.resize({width:pageWidth});
            chart2.resize({width:pageWidth});
        }, 100);
    }
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