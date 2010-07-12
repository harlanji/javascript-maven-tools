/**
 * @constructor
 */
function SafeDispose() {
}
SafeDispose.prototype.burn = function() {};
SafeDispose.prototype.smash = function() {};

/**
 * @constructor 
 * @inherits SafeDispose.burn
 */
function SecureFile() {
}
SecureFile.prototype.shred = function() {};
SecureFile.prototype.burn = SafeDispose.prototype.burn;

/*
{
    files: [
        {
            overview: {
                name: "inherits.js",
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
                alias: "inherits.js",
                returns: [],
                inherits: [],
                properties: [],
                version: "",
                type: "",
                isa: "FILE",
                events: []
            },
            path: "examples/data/inherits.js",
            namespaces: [],
            symbols: [
                {
                    name: "SafeDispose",
                    memberof: "",
                    since: "",
                    classDesc: "",
                    _inheritsFrom: [],
                    file: { //circularReference
                    },
                    methods: [
                        {
                            name: "burn",
                            memberof: "SafeDispose",
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
                            alias: "SafeDispose.burn",
                            returns: [],
                            inherits: [],
                            properties: [],
                            version: "",
                            type: "",
                            isa: "FUNCTION",
                            events: []
                        },
                        {
                            name: "smash",
                            memberof: "SafeDispose",
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
                            alias: "SafeDispose.smash",
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
                    alias: "SafeDispose",
                    returns: [],
                    inherits: [],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "CONSTRUCTOR",
                    events: []
                },
                {
                    name: "burn",
                    memberof: "SafeDispose",
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
                    alias: "SafeDispose.burn",
                    returns: [],
                    inherits: [],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "FUNCTION",
                    events: []
                },
                {
                    name: "smash",
                    memberof: "SafeDispose",
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
                    alias: "SafeDispose.smash",
                    returns: [],
                    inherits: [],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "FUNCTION",
                    events: []
                },
                {
                    name: "SecureFile",
                    memberof: "",
                    since: "",
                    classDesc: "",
                    _inheritsFrom: [],
                    file: { //circularReference
                    },
                    methods: [
                        {
                            name: "shred",
                            memberof: "SecureFile",
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
                            alias: "SecureFile.shred",
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
                    alias: "SecureFile",
                    returns: [],
                    inherits: [ "SafeDispose.burn" ],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "CONSTRUCTOR",
                    events: []
                },
                {
                    name: "shred",
                    memberof: "SecureFile",
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
                    alias: "SecureFile.shred",
                    returns: [],
                    inherits: [],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "FUNCTION",
                    events: []
                }
            ],
            filename: "inherits.js",
            fileGroup: { //circularReference
            }
        }
    ]
}

*/