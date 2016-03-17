'use strict';

/* Services */
var cpServices = angular.module('cpServices', ['ngResource']);
cp.service('sharedDataService', function(){
	// This section is shared between all the controllers
	
	// Shared field description
	this.usedLanguage = 'ita';
	this.name = '';
	this.surname = '';
	this.nickname = '';
	this.ueCitizen = false;
	this.familyAllowances = false;
	this.loading = false;
	this.userIdentity = 'HMTRND69R11Z100M';
	this.base64 = '';
	this.userId = '';
	
	this.gameId = 'game1';
	
	this.practicesEdil = [];
	this.practicesAss = [];
	this.oldPractices = [];
	
	this.allFamilyUpdated = false;
	this.isTest = false;
	
	// Shared messages section
	//-------------------------------------------------------------
	this.msg_text_attention = "";
	this.msg_text_err = "";
	this.msg_text_failure = "";
	this.text_btn_end = "";
	this.text_btn_next = "";
	this.text_btn_close = "";
	this.text_btn_save = "";
	//-------------------------------------------------------------
	
	// Shared time variables
	//-------------------------------------------------------------
	this.three_years_millis = 1000 * 60 * 60 * 24 * 360 * 3;	// I consider an year of 360 days
	this.two_years_millis = 1000 * 60 * 60 * 24 * 360 * 2;
	this.one_year_millis = 1000 * 60 * 60 * 24 * 360; 			// I consider an year of 360 days (12 month of 30 days)
	this.one_year_365_millis = 1000 * 60 * 60 * 24 * 365; 		// I consider an year of 365 days
	this.one_month_millis = 1000 * 60 * 60 * 24 * 30;			// Milliseconds of a month
	this.one_day_millis = 1000 * 60 * 60 * 24 * 2; 				// Milliseconds of a day
	this.six_hours_millis = 1000 * 60 * 60 * 6;					// Milliseconds in six hours
	//-------------------------------------------------------------
	
	this.infoPanelAss = false; // default value: the panel is closed
	this.infoPanelLoc = false; // default value: the panel is closed
	
//	this.searchTab = '';
//	this.searchOpt = '';
//	this.searchVal = '';
//	this.searchList = [];
	
	this.utente = {};
	
	this.idDomanda = '';
	
    this.static_ambiti = [];
    this.static_comuni = [];
    this.static_edizioni = [];
    
    this.scoreTypes = [
          {code: "green leaves", title:"Green Leaves"},
          {code: "health", title:"Health"},
          {code: "p+r", title:"P+R"}
    ];
	
    this.jobs = [ 
         {value:'COLLOCAMENTO', title:'Iscrizione al Collocamento'},
         {value:'LAVORO', title:'Costanza di Lavoro'}
    ];
            
    this.permissions = [
         {value:'SOGGIORNO', title:'Permesso di Soggiorno'},
         {value:'CE', title:'Permesso Ce o Carta di Soggiorno'}
    ];
            
    this.rtypes = [ 
         {value:'NORMALE', title:'Normale'},
         {value:'ALLOGGIO_IMPROPRIAMENTE_ADIBITO', title:'Impropriamente Adibito da almeno 2 anni (soffitti, cantine, sottoscale, auto)'},
         {value:'ALLOGGIO_PRIVO_SERVIZI', title:'Privo di Servizi Igienici o con Servizi Igienici Esterni'}
    ];
            
    this.rtypes_inidoneo = [ 
         {value:'ALLOGGIO_INIDONEO', title:'Inidoneo per numero di stanze da letto'}
    ];
            
    this.rtypes_all = [ 
         {value:'NORMALE', title:'Normale'},              
         {value:'ALLOGGIO_INIDONEO', title:'Inidoneo per numero di stanze da letto'},          
         {value:'ALLOGGIO_IMPROPRIAMENTE_ADIBITO', title:'Impropriamente Adibito da almeno 2 anni (soffitti, cantine, sottoscale, auto)'},
         {value:'ALLOGGIO_PRIVO_SERVIZI', title:'Privo di Servizi Igienici o con Servizi Igienici Esterni'}
    ];
            
    this.genders = [
         'Femminile',
         'Maschile'
    ];
            
    this.municipalities = [
         {code: 1, name: 'Ala'},
         {code: 2, name: 'Avio'},
         {code: 3, name: 'Besenello'},
         {code: 4, name: 'Calliano'},
         {code: 5, name: 'Isera'},
         {code: 6, name: 'Mori'},
         {code: 7, name: 'Nogaredo'},
         {code: 8, name: 'Nomi'},
         {code: 9, name: 'Pomarolo'},
         {code: 10, name: 'Rovereto'},
         {code: 11, name: 'Villa Lagarina'},
         {code: 12, name: 'Volano'},
    ];
            
    this.contracts = [
         {value: 'CANONE_LIBERO', title:'Canone libero (4 anni + 4 anni)'},
         {value: 'CANONE_CONCORDATO', title:'Canone concordato (3 anni + 2 anni)'}
    ];
            
    this.disabilities_under18 = [
         {value: "CATEGORIA_INVALIDA_1", name: '01'},
         {value: "CATEGORIA_INVALIDA_2", name: '05 e 06'},
         {value: "CATEGORIA_INVALIDA_3", name: '07'}
    ];
            
    this.disabilities_over65 = [
         {value: "CATEGORIA_INVALIDA_1", name: '01'},
         {value: "CATEGORIA_INVALIDA_2", name: '05 e 06'},
         {value: "CATEGORIA_INVALIDA_4", name: '08'}
    ];
            
    this.disabilities_all = [
         {value: "CATEGORIA_INVALIDA_1", name: '01'},
         {value: "CATEGORIA_INVALIDA_2", name: '05 e 06'},
         {value: "CATEGORIA_INVALIDA_3", name: '07'},
         {value: "CATEGORIA_INVALIDA_4", name: '08'}
    ];
            
    this.citizenships = [
         {code: 1, name: 'Italiana'},
         {code: 2, name: 'Europea'},
         {code: 3, name: 'Extra UE'},
    ];
            
    this.yes_no = [
         {code: 'true' , title: 'Si'},
         {code: 'false' , title: 'No'}
    ];    
    
    this.yes_no_val = [
         {value: true , title: 'Si'},
         {value: false , title: 'No'}
    ];
            
    this.affinities = [
         {value: 'ALTRO_CONVIVENTE', name: 'Altro convivente'},
         {value: 'PARENTE_34_GRADO', name: 'Parentela 3/4 grado'},
         {value: 'PARENTE_2_GRADO', name: 'Parentela 2 grado'},
         {value: 'PARENTE_1_GRADO', name: 'Parentela 1 grado'},
         {value: 'FIGLIO', name: 'Figlio'},
         {value: 'CONVIVENTE_MORE_UXORIO', name: 'Convivente More Uxorio'},
         {value: 'CONIUGE_NON_SEPARATO', name: 'Coniuge non separato'}          
    ];
            
    this.maritals = [
         {value: 'GIA_CONIUGATO_A', name: 'Gia coniugato/a'},
         {value: 'CONIUGATO_A', name: 'Coniugato/a'},
         {value: 'VEDOVO_A', name: 'Vedovo/a'},
         {value: 'NUBILE_CELIBE', name: 'Nubile/Celibe'}
    ];
    
    this.vallagarinaMunicipality = [
         'ALA',
         'AVIO',
         'BESENELLO',
         'BRENTONICO',
         'CALLIANO',
         'ISERA',
         'MORI',
         'NOGAREDO',
         'NOMI',
         'POMAROLO',
         'RONZO-CHIENIS',
         'ROVERETO',
         'TERRAGNOLO',
         'TRAMBILENO',
         'VALLARSA',
         'VILLA LAGARINA',
         'VOLANO'
    ];
    
    /*this.playersList = [
         {pid:"1", socialId:"1", name:"Raman", surname:"Kazhamiakin", nikname:"Raman1", mail:"kashamiakin@gmail.com"},
         {pid:"43", socialId:"43", name:"Mattia", surname:"Bortolamedi", nikname:"Regolo85", mail:"regolo85@gmail.com"},
         {pid:"67", socialId:"67", name:"Raman", surname:"Kazhamiakin", nikname:"Raman2", mail:"kashamiakin@gmail.com"},
         {pid:"168", socialId:"168", name:"Giuseppe", surname:"Valetto", nikname:"Peppo", mail:"valettofbk@gmail.com"},
         {pid:"157", socialId:"157", name:"Paola", surname:"Rampelotto", nikname:"PaolaR", mail:"paola.rampelotto@gmail.com"},
         {pid:"167", socialId:"167", name:"Annapaola", surname:"Marconi", nikname:"AnnaPaolaM", mail:"annapaola.marconi@gmail.com"},
         {pid:"208", socialId:"208", name:"Mirko", surname:"Perillo", nikname:"MirkoP", mail:"mirko.perillo@gmail.com"}
    ];*/
    this.playerList = [];
	
	// Get and Set methods
	this.getUsedLanguage = function(){
		var value = sessionStorage.language;
		return this.usedLanguage;
	};
	
	this.setUsedLanguage = function(value){
		sessionStorage.language = value;
		this.usedLanguage = value;
	};
	
	this.getPlayersList = function(){
		return this.playersList;
	};
	
	this.setPlayerList = function(list){
		this.playersList = list;
	};
	
	this.getName = function(){
		return this.name;
	};
	
	this.setName = function(value){
		this.name = value;
	};
	
	this.getSurname = function(){
		return this.surname;
	};
	
	this.setSurname = function(value){
		this.surname = value;
	};
	
	this.getNickName = function(){
		return this.nickname;
	};
	
	this.setNickName = function(value){
		this.nickname = value;
	};
	
	this.getUeCitizen = function(){
		return this.ueCitizen;
	};
	
	this.setUeCitizen = function(value){
		this.ueCitizen = value;
	};
	
	this.isFamilyAllowances = function(){
		return this.familyAllowances;
	};
	
	this.setFamilyAllowances = function(value){
		this.familyAllowances = value;
	};
	
	this.isLoading = function(){
		return this.loading;
	};
	
	this.setLoading = function(value){
		this.loading = value;
	};
	
	this.setUserIdentity = function(value){
		//this.userIdentity = value;
		this.utente.codiceFiscale;
	};
	
	this.getVallagarinaMunicipality = function(){
		return this.vallagarinaMunicipality;
	};
	
	this.getStaticAmbiti = function(){
		return this.static_ambiti;
	};
	
	this.getStaticComuni = function(){
		return this.static_comuni;
	};
	
	this.getStaticEdizioni = function(){
		return this.static_edizioni;
	};
	
	this.setStaticAmbiti = function(value){
		this.static_ambiti = value;
	};
	
	this.setStaticComuni = function(value){
		this.static_comuni = value;
	};
	
	this.setStaticEdizioni = function(value){
		this.static_edizioni = value;
	};
	
	this.getScoreTypes = function(){
		return this.scoreTypes;
	};
	
//	this.getUserIdentity = function(){
//		return this.utente.codiceFiscale;
//	};
	
	// ----------------- ONLY FOR TESTS-------------
	this.getUserIdentity = function(){
		if(this.utente.codiceFiscale == null || this.utente.codiceFiscale == ""){
			return this.userIdentity;
		}
		else {
			return this.utente.codiceFiscale;
		}
	};
	//---------------------------------------------
	
	this.setMail = function(value){
		
		this.utente.email = value;
	};	
	
	this.getMail = function(){
		//return this.mail;
		return this.utente.email;
	};
	
	// ----------------- ONLY FOR TESTS-------------
	this.setBase64 = function(value){
		if(value == null || value == ""){
			this.base64 = 'MIIE6TCCA9GgAwIBAgIDBzMlMA0GCSqGSIb3DQEBBQUAMIGBMQswCQYDVQQGEwJJVDEYMBYGA1UECgwPUG9zdGVjb20gUy5wLkEuMSIwIAYDVQQLDBlTZXJ2aXppIGRpIENlcnRpZmljYXppb25lMTQwMgYDVQQDDCtQcm92aW5jaWEgQXV0b25vbWEgZGkgVHJlbnRvIC0gQ0EgQ2l0dGFkaW5pMB4XDTExMTEyMzAwMjQ0MloXDTE3MTEyMjAwNTk1OVowgY4xCzAJBgNVBAYTAklUMQ8wDQYDVQQKDAZUUy1DTlMxJTAjBgNVBAsMHFByb3ZpbmNpYSBBdXRvbm9tYSBkaSBUcmVudG8xRzBFBgNVBAMMPkJSVE1UVDg1TDAxTDM3OFMvNjA0MjExMDE5NzU3MTAwNy53aTRldjVNeCtFeWJtWnJkTllhMVA3ZUtkY1U9MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCsF81BDJjAQat9Lfo/1weA0eePTsEbwTe/0QqlArfOTG3hfLEiSd+mDNsBUJo+cRXZMp677y9a1kYlB+IDY3LGH36Bs1QxM14KA6WB67KX4ZaXENew6Qm7NnkMRboKQiIOUmw1l4OiTETfqKWyFqfAtnyLHd8ZZ6qfjgSsJoSHoQIDAQABo4IB3TCCAdkwge0GA1UdIASB5TCB4jCBrAYFK0wQAgEwgaIwgZ8GCCsGAQUFBwICMIGSDIGPSWRlbnRpZmllcyBYLjUwOSBhdXRoZW50aWNhdGlvbiBjZXJ0aWZpY2F0ZXMgaXNzdWVkIGZvciB0aGUgaXRhbGlhbiBOYXRpb25hbCBTZXJ2aWNlIENhcmQgKENOUykgcHJvamVjdCBpbiBhY2NvcmRpbmcgdG8gdGhlIGl0YWxpYW4gcmVndWxhdGlvbiAwMQYGK0wLAQMBMCcwJQYIKwYBBQUHAgEWGWh0dHA6Ly9wb3N0ZWNlcnQucG9zdGUuaXQwOgYIKwYBBQUHAQEELjAsMCoGCCsGAQUFBzABhh5odHRwOi8vcG9zdGVjZXJ0LnBvc3RlLml0L29jc3AwDgYDVR0PAQH/BAQDAgeAMBMGA1UdJQQMMAoGCCsGAQUFBwMCMB8GA1UdIwQYMBaAFO5h8R6jQnz/4EeFe3FeW6ksaogHMEYGA1UdHwQ/MD0wO6A5oDeGNWh0dHA6Ly9wb3N0ZWNlcnQucG9zdGUuaXQvY25zL3Byb3ZpbmNpYXRyZW50by9jcmwuY3JsMB0GA1UdDgQWBBRF3Z13QZAmn85HIYPyIg3QE8WM2DANBgkqhkiG9w0BAQUFAAOCAQEAErn/asyA6AhJAwOBmxu90umMNF9ti9SX5X+3+pcqLbvKOgCNfjhGJZ02ruuTMO9uIi0DIDvR/9z8Usyf1aDktYvyrMeDZER+TyjviA3ntYpFWWIh1DiRnAxuGYf6Pt6HNehodf1lhR7TP+iejH24kS2LkqUyiP4J/45sTK6JNMXPVT3dk/BAGE1cFCO9FI3QyckstPp64SEba2+LTunEEA4CKPbTQe7iG4FKpuU6rqxLQlSXiPVWZkFK57bAUpVL/CLc7unlFzIccjG/MMvjWcym9L3LaU//46AV2hR8pUfZevh440wAP/WYtomffkITrMNYuD1nWxL7rUTUMkvykw==';
		} else {
			this.base64 = value;
		}
	};
	//----------------------------------------------
	
//	this.setBase64 = function(value){
//		this.base64 = value;
//	};
	
	
	this.getBase64 = function(){
		return this.base64;
	};
	
	this.setUtente = function(nome, cognome, sesso, dataNascita, provinciaNascita, luogoNascita, codiceFiscale, cellulare, email, indirizzoRes, capRes, cittaRes, provinciaRes){
		this.utente.nome = nome;
		this.utente.cognome = cognome;
		this.utente.sesso = sesso;
		this.utente.dataNascita = dataNascita;
		this.utente.provinciaNascita = provinciaNascita;
		this.utente.luogoNascita = luogoNascita;
		this.utente.codiceFiscale = codiceFiscale;
		this.utente.cellulare = cellulare;
		if(email != null && email != ""){
			this.utente.email = email;
		}
		this.utente.indirizzoRes = indirizzoRes;
		this.utente.capRes = capRes; 
		this.utente.cittaRes = cittaRes; 
		this.utente.provinciaRes = provinciaRes;
	};
	
	this.getUtente = function(){
		return this.utente;
	};
	
	this.setIdDomanda = function(value){
		this.idDomanda = value;
	};
	
	this.getIdDomanda = function(){
		return this.idDomanda;
	};
	
	// Lists getters
	this.getJobs = function(){
		return this.jobs;
	};
	
	this.getPermissions = function(){
		return this.permissions;
	};
	
	this.getRtypes = function(){
		return this.rtypes;
	};
	
	this.getRtypesInidoneo = function(){
		return this.rtypes_inidoneo;
	};
	
	this.getRtypesAll = function(){
		return this.rtypes_all;
	};
	
	this.getGenders = function(){
		return this.genders;
	};
	
	this.getMunicipalities = function(){
		return this.municipalities;
	};
	
	this.getContracts = function(){
		return this.contracts;
	};
	
	this.getDisabilities_under18 = function(){
		return this.disabilities_under18;
	};
	
	this.getDisabilities_all = function(){
		return this.disabilities_all;
	};
	
	this.getDisabilities_over65 = function(){
		return this.disabilities_over65;
	};
	
	this.getCitizenships = function(){
		return this.citizenships;
	};
	
	this.getYesNo = function(){
		return this.yes_no;
	};
	
	this.getYesNoVal = function(){
		return this.yes_no_val;
	};
	
	this.getAffinities = function(){
		return this.affinities;
	};
	
	this.getMaritals = function(){
		return this.maritals;
	};
	
	this.getInfoPanelAss = function(){
		return this.infoPanelAss;
	};
	
	this.getInfoPanelLoc = function(){
		return this.infoPanelLoc;
	};
	
	this.setInfoPanelAss = function(value){
		this.infoPanelAss = value;
	};
	
	this.setInfoPanelLoc = function(value){
		this.infoPanelLoc = value;
	};
	
	this.getThreeYearsMillis = function(){
		return this.three_years_millis;
	};
	
	this.getTwoYearsMillis = function(){
		return this.two_years_millis;
	};
	
	this.getOneYearMillis = function(){
		return this.one_year_millis;
	};
	
	this.getOneYear365Millis = function(){
		return this.one_year_365_millis;
	};
	
	this.getOneMonthMillis = function(){
		return this.one_month_millis;
	};
	
	this.getOneDayMillis = function(){
		return this.one_day_millis;
	};
	
	this.getSixHoursMillis = function(){
		return this.six_hours_millis;
	};
	
	this.getPracticesEdil = function(){
		return this.practicesEdil;
	};
	
	this.setPracticesEdil = function(list){
		this.practicesEdil = list;
	};
	
	this.getPracticesAss = function(){
		return this.practicesAss;
	};
	
	this.setPracticesAss = function(list){
		this.practicesAss = list;
	};
	
	this.getOldPractices = function(){
		return this.oldPractices;
	};
	
	this.setOldPractices = function(list){
		this.oldPractices = list;
	};
	
	this.setAllFamilyUpdate = function(value){
		this.allFamilyUpdated = value;
	};
	
	this.getAllFamilyUpdate = function(){
		return this.allFamilyUpdated;
	};
	
	this.setIsTest = function(value){
		this.isTest = value;
	};
	
	this.getIsTest = function(){
		return this.isTest;
	};
	
	this.setUserId = function(value){
		this.userId = value;
	};
	
	this.getUserId = function(){
		return this.userId;
	};
	
	this.setGameId = function(value){
		this.gameId = value;
	};
	
	this.getGameId = function(){
		return this.gameId;
	};
	
	
	//Getters and Setters Methods for messages
	this.getMsgTextAttention = function(){
		return this.msg_text_attention;
	};
	
	this.setMsgTextAttention = function(value){
		this.msg_text_attention = value;
	};
	
	this.getMsgTextErr = function(){
		return this.msg_text_err;
	};
	
	this.setMsgTextErr = function(value){
		this.msg_text_err = value;
	};
	
	this.getMsgTextFailure = function(){
		return this.msg_text_failure;
	};
	
	this.setMsgTextFailure = function(value){
		this.msg_text_failure = value;
	};
	
	this.getTextBtnEnd = function(){
		return this.text_btn_end;
	};
	
	this.setTextBtnEnd = function(value){
		this.text_btn_end = value;
	};
	
	this.getTextBtnNext = function(){
		return this.text_btn_next;
	};
	
	this.setTextBtnNext = function(value){
		this.text_btn_next = value;
	};
	
	this.getTextBtnClose = function(){
		return this.text_btn_close;
	};
	
	this.setTextBtnClose = function(value){
		this.text_btn_close = value;
	};
	
	this.getTextBtnSave = function(){
		return this.text_btn_save;
	};
	
	this.setTextBtnSave = function(value){
		this.text_btn_save = value;
	};
	
	//Method that inithialize all the messages used in myweb
	this.inithializeAllMessages = function(data){
		var endMessages = false;
		for(var i = 0; (i < data.length) && (!endMessages); i++){
			switch (data[i].key){
				case "msg_text_attention":
					this.setMsgTextAttention(data[i].value);
					break;
				case "msg_text_err":
					this.setMsgTextErr(data[i].value);
					break;
				case "msg_text_failure":
					this.setMsgTextFailure(data[i].value);
					break;	
				case "text_btn_end":
					this.setTextBtnEnd(data[i].value);
					break;
				case "text_btn_next":
					this.setTextBtnNext(data[i].value);
					break;	
				case "text_btn_close":
					this.setTextBtnClose(data[i].value);
					break;
				case "text_btn_save":
					this.setTextBtnSave(data[i].value);
					break;
					
				default: endMessages = true; break;
				
			}
			
		}
	};
	
});

//Message retriever method
cp.factory('getMyMessages', function($http, $q) {
	
	//var _this = this;

    var promiseToHaveData = function() {
        var deferred = $q.defer();
        
        var fileJson = '';
        if(this.usedLanguage == 'ita'){
        	fileJson = 'i18n/resources-locale_it-IT.json';
        } else {
        	fileJson = 'i18n/resources-locale_en-US.json';
        }

        $http.get(fileJson)
            .success(function(data) {
                //angular.extend(_this, data);
                deferred.resolve(data);
                // Funzione di caricamento stringhe messaggi in variabili di service
                //console.log("Finded message data: " + JSON.stringify(data));
            })
            .error(function() {
                deferred.reject('could not find someFile.json');
                console.log("Error in message data recovery.");
            });

        return deferred.promise;
    };
    return {promiseToHaveData : promiseToHaveData};

});

// Proxy Methods section
cp.factory('invokeWSService', function($http, $q) {
	
	var url = '/service.epu/';
	var getProxy = function(method, funcName, params, headers, data){
		var deferred = $q.defer();
		$http({
			method : method,
			url : url + funcName,
			params : params,
			headers : headers,
			data : data
		}).success(function(data) {
			//console.log("Returned data ok: " + JSON.stringify(data));
			deferred.resolve(data);
		}).error(function(data) {
			console.log("Returned data FAIL: " + JSON.stringify(data));
			deferred.resolve(data);
		});
		return deferred.promise;
	};
	return {getProxy : getProxy};
});

cp.factory('invokeWSServiceProxy', function($http, $q) {
	var getProxy = function(method, funcName, params, headers, data){
		var deferred = $q.defer();
		
		var urlWS = funcName;
		if(params != null){
			urlWS += '?';
			for(var propertyName in params) {
				urlWS += propertyName + '=' + params[propertyName];
				urlWS += '&';
			};
			urlWS = urlWS.substring(0, urlWS.length - 1); // I remove the last '&'
		}
		//console.log("Proxy Service: url completo " + urlWS);
		
		if(method == 'GET' && params != null){
			$http({
				method : method,
				url : 'rest/allGet',
				params : {
					"urlWS" : urlWS + '&noCache=' + new Date().getTime()	// quela mer.. de ie el cacheava tut e con sta modifica el funzia
				},
				headers : headers
			}).success(function(data) {
				//console.log("Returned data ok: " + JSON.stringify(data));
				deferred.resolve(data);
			}).error(function(data) {
				console.log("Returned data FAIL: " + JSON.stringify(data));
				deferred.resolve(data);
			});
		} else if(method == 'GET' && params == null){
			$http({
				method : method,
				url : 'rest/allGet',
				params : {
					"urlWS" : urlWS + '?noCache=' + new Date().getTime()	// quela mer.. de ie el cacheava tut e con sta modifica el funzia
				},
				headers : headers
			}).success(function(data) {
				//console.log("Returned data ok: " + JSON.stringify(data));
				deferred.resolve(data);
			}).error(function(data) {
				console.log("Returned data FAIL: " + JSON.stringify(data));
				deferred.resolve(data);
			});
		} else {
			$http({
				method : method,
				url : 'rest/allPost',
				params : {
					"urlWS" : urlWS,
				},
				headers : headers,
				data : data
			}).success(function(data) {
				//console.log("Returned data ok: " + JSON.stringify(data));
				deferred.resolve(data);
			}).error(function(data) {
				console.log("Returned data FAIL: " + JSON.stringify(data));
				deferred.resolve(data);
			});
		}
		return deferred.promise;
	};
	return {getProxy : getProxy};
});

cp.factory('invokeWSNiksServiceProxy', function($http, $q) {
	var getProxy = function(method, funcName, params, headers, data){
		var deferred = $q.defer();
		
		var urlWS = funcName;
		if(params != null){
			urlWS += '?';
			for(var propertyName in params) {
				urlWS += propertyName + '=' + params[propertyName];
				urlWS += '&';
			};
			urlWS = urlWS.substring(0, urlWS.length - 1); // I remove the last '&'
		}
		//console.log("Proxy Service: url completo " + urlWS);
		
		if(method == 'GET' && params != null){
			$http({
				method : method,
				url : 'rest/allNiks',
				params : {
					"urlWS" : urlWS + '&noCache=' + new Date().getTime()	// quela mer.. de ie el cacheava tut e con sta modifica el funzia
				},
				headers : headers
			}).success(function(data) {
				//console.log("Returned data niks ok: " + JSON.stringify(data));
				deferred.resolve(data);
			}).error(function(data) {
				console.log("Returned data FAIL: " + JSON.stringify(data));
				deferred.resolve(data);
			});
		} else if(method == 'GET' && params == null){
			$http({
				method : method,
				url : 'rest/allNiks',
				params : {
					"urlWS" : urlWS + '?noCache=' + new Date().getTime()	// quela mer.. de ie el cacheava tut e con sta modifica el funzia
				},
				headers : headers
			}).success(function(data) {
				//console.log("Returned data niks ok: " + JSON.stringify(data));
				deferred.resolve(data);
			}).error(function(data) {
				console.log("Returned data FAIL: " + JSON.stringify(data));
				deferred.resolve(data);
			});
		} else {
			$http({
				method : method,
				url : 'rest/updateNick',
				params : {
					"urlWS" : urlWS,
				},
				headers : headers,
				data : data
			}).success(function(data) {
				deferred.resolve(data);
			}).error(function(data) {
				console.log("Returned data FAIL: " + JSON.stringify(data));
				deferred.resolve(data);
			});
		}
		return deferred.promise;
	};
	return {getProxy : getProxy};
});

cp.factory('invokePdfServiceProxy', function($http, $q) {
	var getProxy = function(method, funcName, params, headers, data){
		var deferred = $q.defer();
		
		$http({
			method : method,
			url : funcName,
			params : params,
			headers : headers,
			data : data
		}).success(function(data) {
			//console.log("Returned data ok: " + JSON.stringify(data));
			deferred.resolve(data);
		}).error(function(data) {
			console.log("Returned data FAIL: " + JSON.stringify(data));
			deferred.resolve(data);
		});
		return deferred.promise;
	};
	return {getProxy : getProxy};
	
});
