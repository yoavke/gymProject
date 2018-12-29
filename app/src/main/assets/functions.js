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
    let my_foot = document.querySelector(".foot");
    my_foot.innerHTML = '<a href="index.html" data-ajax="false">Go Home</a> HIT Android Project - Jonathan Hakim, Yoav Keren.';
}