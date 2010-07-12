framework.addTo(
	"FileWatcher.Widget",
	/** @scope FileWatcher.Widget */
	{
		/** Set up the widget. */
		initialize: function(container, args) {
		}
	}
);

Builder.make(
	"Sorter",
	/** @scope Sorter.prototype */
	{
		/** Register a sorting function. */
		register: function(f) {
		}
	}
);

function Sorter() {
	this.register = function(f) {
	}
}

var Record = function() {
	return /** @scope Record */ {
		getRecord: function() {}
	};
}();

/*
{
    files: [
        {
            overview: {
                name: "scope.js",
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
                alias: "scope.js",
                returns: [],
                inherits: [],
                properties: [],
                version: "",
                type: "",
                isa: "FILE",
                events: []
            },
            path: "examples/data/scope.js",
            namespaces: [],
            symbols: [
                {
                    name: "FileWatcher.Widget.initialize",
                    memberof: "",
                    since: "",
                    classDesc: "",
                    _inheritsFrom: [],
                    file: { //circularReference
                    },
                    methods: [],
                    params: [
                        {
                            title: "param",
                            desc: "",
                            type: "",
                            name: "container",
                            isOptional: false
                        },
                        {
                            title: "param",
                            desc: "",
                            type: "",
                            name: "args",
                            isOptional: false
                        }
                    ],
                    deprecated: "",
                    desc: "Set up the widget.",
                    exceptions: [],
                    doc: { tags: [] },
                    see: [],
                    augments: [],
                    alias: "FileWatcher.Widget.initialize",
                    returns: [],
                    inherits: [],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "FUNCTION",
                    events: []
                },
                {
                    name: "register",
                    memberof: "Sorter",
                    since: "",
                    classDesc: "",
                    _inheritsFrom: [],
                    file: { //circularReference
                    },
                    methods: [],
                    params: [
                        { title: "param", desc: "", type: "", name: "f", isOptional: false }
                    ],
                    deprecated: "",
                    desc: "Register a sorting function.",
                    exceptions: [],
                    doc: { tags: [] },
                    see: [],
                    augments: [],
                    alias: "Sorter.register",
                    returns: [],
                    inherits: [],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "FUNCTION",
                    events: []
                },
                {
                    name: "Sorter",
                    memberof: "",
                    since: "",
                    classDesc: "",
                    _inheritsFrom: [],
                    file: { //circularReference
                    },
                    methods: [
                        {
                            name: "register",
                            memberof: "Sorter",
                            since: "",
                            classDesc: "",
                            _inheritsFrom: [],
                            file: { //circularReference
                            },
                            methods: [],
                            params: [
                                { title: "param", desc: "", type: "", name: "f", isOptional: false }
                            ],
                            deprecated: "",
                            desc: "undocumented",
                            exceptions: [],
                            doc: { tags: [] },
                            see: [],
                            augments: [],
                            alias: "Sorter.register",
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
                    desc: "undocumented",
                    exceptions: [],
                    doc: { tags: [] },
                    see: [],
                    augments: [],
                    alias: "Sorter",
                    returns: [],
                    inherits: [],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "FUNCTION",
                    events: []
                },
                {
                    name: "register",
                    memberof: "Sorter",
                    since: "",
                    classDesc: "",
                    _inheritsFrom: [],
                    file: { //circularReference
                    },
                    methods: [],
                    params: [
                        { title: "param", desc: "", type: "", name: "f", isOptional: false }
                    ],
                    deprecated: "",
                    desc: "undocumented",
                    exceptions: [],
                    doc: { tags: [] },
                    see: [],
                    augments: [],
                    alias: "Sorter.register",
                    returns: [],
                    inherits: [],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "FUNCTION",
                    events: []
                },
                {
                    name: "Record.getRecord",
                    memberof: "",
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
                    alias: "Record.getRecord",
                    returns: [],
                    inherits: [],
                    properties: [],
                    version: "",
                    type: "",
                    isa: "FUNCTION",
                    events: []
                }
            ],
            filename: "scope.js",
            fileGroup: { //circularReference
            }
        }
    ]
}

*/