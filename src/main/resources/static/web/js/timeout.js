function idleLogout() {
    var t;
    window.onload = resetTimer;
    window.onmousemove = resetTimer;
    window.onmousedown = resetTimer;  // catches touchscreen presses as well
    window.ontouchstart = resetTimer; // catches touchscreen swipes as well
    window.onclick = resetTimer;      // catches touchpad clicks as well
    window.onkeypress = resetTimer;
    window.addEventListener('scroll', resetTimer, true); // improved; see comments


    function logout() {
        // your function for too long inactivity goes here
        // e.g. window.location.href = 'logout.php';
        Swal.fire({
            title: 'Se ha desconectado por inactividad',
        }).then((result)=>{
        if(result){
            axios.post('/api/logout')
                .then(response => window.location.href="/web/index.html")
                .catch(() =>{
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        }
        })
    }

    function resetTimer() {
        clearTimeout(t);
        t = setTimeout(logout, 80000);  // time is in milliseconds
    }


}
idleLogout();