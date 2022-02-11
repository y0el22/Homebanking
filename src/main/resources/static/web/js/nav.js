fetch('nav.html')
.then(res => res.text())
.then(text => {
    errorToats: null,
    errorMsg: null,
    let oldelem = document.querySelector("script#replace_with_navbar");
    let newelem = document.createElement("div");
    newelem.innerHTML = text;
    oldelem.parentNode.replaceChild(newelem,oldelem);
    methods:{
    signOut: function(){
                axios.post('/api/logout')
                    .then(response => window.location.href="/web/index.html")
                    .catch(() =>{
                        this.errorMsg = "Sign out failed"
                        this.errorToats.show();
                    })
            },
           },
})
