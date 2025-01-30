{
        "status": 400,
        "message": "Validation failed",
        "isSuccess": false,
        "errors": [
        {
        "field": "firstName",
        "value": "",
        "message": "First name is required"
        },
        {
        "field": "email",
        "value": "invalid-email",
        "message": "Invalid email format"
        },
        {
        "field": "userRelations[1].relationType",
        "value": "",
        "message": "Relation type is required"
        },
        {
        "field": "userRelations[1].relatedUserId",
        "value": "null",
        "message": "Related user ID cannot be null"
        }
        ]
        }
