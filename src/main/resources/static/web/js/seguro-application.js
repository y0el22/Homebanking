var app = new Vue({
    el:"#app",
    data:{
        seguroTypes: [],
        seguroTypeId: 0,
        clientAccounts: [],
        errorToats: null,
        errorMsg: null,
        accountToNumber: "VIN",
        valor: 0,
    },
    methods:{
        getData: function(){
            Promise.all([axios.get("/api/seguros"),axios.get("/api/clients/current/accounts")])
            .then((response) => {
                //get loan types ifo
                this.seguroTypes = response[0].data;
                this.clientAccounts = response[1].data;
            })
            .catch((error) => {
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        checkApplication: function(){
            if(this.seguroTypeId == 0){
                console.log("1");
                this.errorMsg = "You must select a secure type";
                this.errorToats.show();
            }
            else if(this.accountToNumber == "VIN"){
                console.log("2");
                this.errorMsg = "You must indicate an account";
                this.errorToats.show();
            }

            else{
                console.log("3");
                this.modal.show();
            }
        },
        apply: function(){
            axios.post("/api/seguros",{seguroId: this.seguroTypeId, toAccountNumber: this.accountToNumber})
            .then(response => {
                this.modal.hide();
                this.okmodal.show();
            })
            .catch((error) =>{
                this.errorMsg = error.response.data;  
                this.errorToats.show();
            })
        },

        finish: function(){
            window.location.reload();
        },

        signOut: function(){
            axios.post('/api/logout')
            .then(response => window.location.href="/web/index.html")
            .catch(() =>{
                this.errorMsg = "Sign out failed"   
                this.errorToats.show();
            })
        },
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.modal = new bootstrap.Modal(document.getElementById('confirModal'));
        this.okmodal = new bootstrap.Modal(document.getElementById('okModal'));
        this.getData();
    }
})