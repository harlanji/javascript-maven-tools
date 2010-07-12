/**
	@constructor
*/
function Filebox() {
	this.add = function() {
	}
	this.remove = function() {
	}
}

SecureFilebox.prototype = new Filebox();
SecureFilebox.prototype.constructor = SecureFilebox;

/**
	@constructor 
	@augments Filebox
*/
function SecureFilebox() {
	this.shred = function() {
	}
}

/*
{
    files: [
        {
            overview: {
                name: "augments.js",
                memberof: "",
                since: "",
                classDesc: "",
                _inheritsFrom: [],
                file: {},
                methods: [],
                params: [],
                deprecated: "",
                desc: "",
                exceptions: [],
                doc: { tags: [] },
                see: [],
                augments: [],
                alias: "augments.js",
                returns: [],
                inherits: [],
                properties: [],
                version: "",
                type: "",
                isa: "FILE",
                events: []
            },
            path: "examples/data/augments.js",
            namespaces: [],
            symbols: [
                {
                    name: "Filebox",
                    memberof: "",
                    since: "",
                    classDesc: "",
                    _inheritsFrom: [],
                    file: { //circularReference
                    },
                    methods: [
                        {
                            name: "add",
                            memberof: "Filebox",
                            since: "",
                            classDesc: "",
                            _inheritsFrom: [],
                            file: { //circularReference
                            },
                            methods: [],
                            params: [],
                            deprecated: "",
                            desc: "undocumented",
                            exceptions: [],
                            doc: { tags: [] },
                            see: [],
                            augments: [],
                            alias: "Filebox.add",
                            returns: [],
                            inherits: [],
                            properties: [],
                            version: "",
                            type: "",
                            isa: "FUNCTION",
                            events: []
                        },
                        {
                            name: "remove",
                            memberof: "Filebox",
                            since: "",
                            classDesc: "",
                            _inheritsFrom: [],
                            file: { //circularReference
                            },
                            methods: [],
                            params: [],
                            deprecated: "",
                            desc: "undocumented",
                            exceptions: [],
                            doc: { tags: [] },
                            see: [],
                            augments: [],
                            alias: "Filebox.remove",
                            returns: [],
                            inherits: [],
                            properties: [],
                            version: "",
                            type: "",
                            isa: "FUNCTION",
                            events: []
                        }
                    ],
                    params: [],
                    deprecated: "",
                    desc: "",
                    exceptions: [],
                    doc: { tags: [] },
                    see: [],
                    augments: [],
                    alias: "Filebox",
                    returns: [],
                    inherits: [],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "CONSTRUCTOR",
                    events: []
                },
                {
                    name: "add",
                    memberof: "Filebox",
                    since: "",
                    classDesc: "",
                    _inheritsFrom: [],
                    file: { //circularReference
                    },
                    methods: [],
                    params: [],
                    deprecated: "",
                    desc: "undocumented",
                    exceptions: [],
                    doc: { tags: [] },
                    see: [],
                    augments: [],
                    alias: "Filebox.add",
                    returns: [],
                    inherits: [],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "FUNCTION",
                    events: []
                },
                {
                    name: "remove",
                    memberof: "Filebox",
                    since: "",
                    classDesc: "",
                    _inheritsFrom: [],
                    file: { //circularReference
                    },
                    methods: [],
                    params: [],
                    deprecated: "",
                    desc: "undocumented",
                    exceptions: [],
                    doc: { tags: [] },
                    see: [],
                    augments: [],
                    alias: "Filebox.remove",
                    returns: [],
                    inherits: [],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "FUNCTION",
                    events: []
                },
                {
                    name: "SecureFilebox",
                    memberof: "",
                    since: "",
                    classDesc: "",
                    _inheritsFrom: [],
                    file: { //circularReference
                    },
                    methods: [
                        {
                            name: "shred",
                            memberof: "SecureFilebox",
                            since: "",
                            classDesc: "",
                            _inheritsFrom: [],
                            file: { //circularReference
                            },
                            methods: [],
                            params: [],
                            deprecated: "",
                            desc: "undocumented",
                            exceptions: [],
                            doc: { tags: [] },
                            see: [],
                            augments: [],
                            alias: "SecureFilebox.shred",
                            returns: [],
                            inherits: [],
                            properties: [],
                            version: "",
                            type: "",
                            isa: "FUNCTION",
                            events: []
                        }
                    ],
                    params: [],
                    deprecated: "",
                    desc: "",
                    exceptions: [],
                    doc: { tags: [] },
                    see: [],
                    augments: [ "Filebox" ],
                    alias: "SecureFilebox",
                    returns: [],
                    inherits: [],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "CONSTRUCTOR",
                    events: []
                },
                {
                    name: "shred",
                    memberof: "SecureFilebox",
                    since: "",
                    classDesc: "",
                    _inheritsFrom: [],
                    file: { //circularReference
                    },
                    methods: [],
                    params: [],
                    deprecated: "",
                    desc: "undocumented",
                    exceptions: [],
                    doc: { tags: [] },
                    see: [],
                    augments: [],
                    alias: "SecureFilebox.shred",
                    returns: [],
                    inherits: [],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "FUNCTION",
                    events: []
                }
            ],
            filename: "augments.js",
            fileGroup: { //circularReference
            }
        }
    ]
}

*/