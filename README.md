	1. H2 was used as in memory DB
	2. As central-bank MS APIs were not defined I do not know what exact response they return. 
	For this case I chose BigDecimal for both , in case the threshold and regional info are trillions, of course the program can be updated accordingly in case my assumption is wrong.
	3. Please see below the swagger for the api:
	{
	  "swagger": "2.0",
	  "info": {
	    "title": "Wealth Rating Managment",
	    "version": "2.1.0"   
	  },
	  "schemes": [
	    "http"
	  ],  
	  "paths": {
	    "/wealth-rating/manage": {
	      "post": {
	        "tags": [
	          "Wealth Rating Managment"
	        ],
	        "description": "Used to manage wealth rating",
	        "parameters": [
	          {
	            "name": "WealthRatingManagment",
	            "in": "body",
	            "schema": {
	              "$ref": "#/definitions/PersonalData"
	            },
	            "description": "Wealth Rating Managment"
	          }
	        ],
	        "responses": {
	          "200": {
	            "$ref": "#/responses/200Response"
	          },          
	          "500": {
	            "$ref": "#/responses/500Response"
	          }
	        }
	      }
	    }
	  },
	  "responses": {    
	    "200Response": {
	      "description": "Response 200",
	      "schema": {
	        "$ref": "#/definitions/Error"
	      }
	    },   
	    "500Response": {
	      "description": "Response 500",
	      "schema": {
	        "$ref": "#/definitions/Error"
	      }
	    }
	  },
	  "definitions": {  
	    "personalInfo": {
	      "description": "Personal Information of the person received in the request",
	      "title": "personalInfo",
	      "type": "object",
	      "properties": {
	        "firstName": {
	          "description": "First Name",
	          "type": "string",
	          "example": "Alona"
	        },
	        "lastName": {
	          "description": "Last Name",
	          "type": "string",
	          "example": "Brichko"
	        },
	        "city": {
	          "description": "City",
	          "type": "string",
	          "example": "Herzliya"
	        }
	      },
	      "required": [
	        "firstName",
	        "lastName",
	        "city"
	      ],
	      "additionalProperties": false
	    },
	    "financialInfo": {
	      "description": "Financial Information of the person received in the request",
	      "title": "financialInfo",
	      "type": "object",
	      "properties": {
	        "cash": {
	          "description": "First Name",
	          "type": "number",
	          "example": 125000000000
	        },
	        "numberOfAssets": {
	          "description": "Last Name",
	          "type": "number",
	          "example": 55
	        }
	      },
	      "required": [
	        "cash",
	        "numberOfAssets"
	      ],
	      "additionalProperties": false
	    },    
	    "PersonalData": {
	      "description": "Personal Data of the person received in the request",
	      "title": "PersonalData",
	      "type": "object",
	      "required": [
	        "id",
	        "personalInfo",
	        "financialInfo"
	      ],
	      "properties": {
	        "id": {
	          "description": "id",
	          "type": "number",
	          "example": 319365987
	        },
	        "personalInfo": {
	          "$ref": "#/definitions/personalInfo"
	        },
	        "financialInfo": {
	          "$ref": "#/definitions/financialInfo"
	        }
	      }
	    }, 
	    "Error": {
	      "type": "string",
	      "description": "Error",
	      "title": "Error"
	    }
	  }
}
