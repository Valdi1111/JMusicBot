function t(e, t) {
    return locales[locale] ? locales[locale][e] || e : e
}

window.resetTheme = function () {
    window.localStorage && (window.localStorage.theme = "simplex"), window.location.href = "/"
};
var currentInstanceId = null, $stateProviderRef = null, ctrX = 0, globalNotice,
    tmpInstance = window.location.hash.match(/instance=([^&]+)$/);
window.localStorage && tmpInstance && (window.localStorage.instanceId = tmpInstance[1]);
var locale = window.localStorage ? window.localStorage.language || "en-US" : "en-US";
if (!locales) var locales = {};
locales["en-US"] = {
    __name: "English",
    __code: "en-US"
}, angular.module("sso", []).controller("sso", ["$scope", "$http", "$timeout", function (e, t, n) {
    e.loginInfo = "", t({
        method: "POST",
        url: "/api/v1/bot/login",
        data: {transitToken: window.location.search.substr(1)}
    }).then(function (t) {
        t.data.token ? (window.localStorage && (window.localStorage.loginBotId = t.data.botId), window.localStorage && (window.localStorage.token = t.data.token), e.loginInfo = "Success.", window.location.href = "/") : (e.loginInfo = "Failed.", n(function () {
            window.location.href = "/"
        }, 2e3))
    })
}]);
var mod = "ts3soundboard-bot";
if (window.localStorage && window.localStorage.ko && (mod = "."), angular.module(mod, ["ngTouch", "mgcrea.ngStrap", "ui.router", "angularFileUpload", "angularMoment", "ts3soundboard-bot-partials", "ngDraggable", "http-auth-interceptor", "ngProgressLite", "ngAnimate", "ui.slider", "cfp.hotkeys", "infinite-scroll", "angularLoad", "ngSanitize", "ui.select"]).run(["$http", function (e) {
}]).factory("RecursionHelper", ["$compile", function (e) {
    return {
        compile: function (t, n) {
            angular.isFunction(n) && (n = {post: n});
            var a, o = t.contents().remove();
            return {
                pre: n && n.pre ? n.pre : null, post: function (t, r) {
                    a || (a = e(o)), a(t, function (e) {
                        r.append(e)
                    }), n && n.post && n.post.apply(null, arguments)
                }
            }
        }
    }
}]).directive("convertToNumber", function () {
    return {
        require: "ngModel", link: function (e, t, n, a) {
            a.$parsers.push(function (e) {
                return null != e ? parseInt(e, 10) : null
            }), a.$formatters.push(function (e) {
                return null != e ? "" + e : null
            })
        }
    }
}).directive("scriptParams", ["RecursionHelper", function (e) {
    return {
        scope: {
            settings: "=settings",
            var: "=var",
            files: "=",
            channels: "=",
            channelsFound: "=",
            conditionsTrue: "="
        }, link: function (e, t, n, a) {
        }, compile: function (t) {
            return e.compile(t)
        }, transclude: !0, templateUrl: "/partials/settings.scripts.params.tpl.html"
    }
}]).directive("trackscroll", function () {
    return {
        restrict: "A", link: function (e, t, n) {
            var a = $("body");
            e.$watch("selected", function (n, o) {
                if ("mouse" != e.selectSource && n && n !== o) {
                    var r = t.find("tr").eq(n).position().top, s = a.scrollTop(),
                        i = a.scrollTop() + $(window).height() - $(window).height() / 4;
                    (r < s || r > i) && window.scrollTo(0, r - $(window).height() / 2)
                }
            })
        }
    }
}).filter("t", ["$window", function (e) {
    return function (e, t) {
        return locales[locale] ? locales[locale][e] || e : e
    }
}]).filter("kvfilter", function () {
    return function (e, n) {
        var a = {};
        return n = n.toLowerCase(), angular.forEach(e, function (e, o) {
            (e.toLowerCase().indexOf(n) >= 0 || (t(o).toLowerCase() || "").indexOf(n) >= 0) && (a[o] = e)
        }), a
    }
}).directive("autofocus", ["$timeout", function (e) {
    return {
        restrict: "A", link: function (t, n) {
            e(function () {
                n[0].focus()
            }, 50)
        }
    }
}]).config(["$provide", "uiSelectConfig", function (e, t) {
    "use strict";
    t.theme = "bootstrap", e.decorator("$modal", ["$controller", "$delegate", "$injector", "$q", "$rootScope", function (e, t, n, a, o) {
        function r(r) {
            var s, i, l = [], c = window._.omit(r, ["controller", "controllerAs", "resolve"]), u = c.scope || o;
            return r.resolve && (l = window._.map(r.resolve, function (e) {
                return n.invoke(e)
            })), r.controller && (i = a.all(l).then(function (t) {
                var n = {}, a = 0;
                window._.forEach(r.resolve, function (e, o) {
                    n[o] = t[a++]
                }), u = u.$new(), n.$scope = u, s = e(r.controller, n), r.controllerAs && (u[r.controllerAs] = s)
            })), i.then(function () {
                window._.assign(c, {scope: u});
                var e = t(c);
                return u.$hide = e.hide, e
            })
        }

        return t.open = r, t
    }])
}]).config(["$stateProvider", "$urlRouterProvider", "$locationProvider", "$httpProvider", function (e, n, a, o) {
    o.interceptors.push("progressInterceptor"), $stateProviderRef = e, a.html5Mode(!0), n.otherwise("/play/files").when("/settings", "/settings/personal").when("/play", "/play/files"), e.state("app", {
        abstract: !0,
        template: '<div ui-view class="base-content"></div>',
        resolve: {
            instances: ["$http", function (e) {
                return e({url: "/api/v1/bot/instances"}).then(function (e) {
                    for (var t = e.data, n = currentInstanceId || window.localStorage.instanceId, a = 0; a < t.length; a++) t[a].uuid == n && (currentInstanceId = t[a].uuid);
                    return currentInstanceId || (currentInstanceId = t[0].uuid), t
                })
            }]
        }
    }).state("app.help", {
        url: "/help",
        templateUrl: "/partials/help.tpl.html",
        controller: ["$scope", "$state", "$stateParams", "help", "$timeout", function (e, t, n, a, o) {
            if (e.help = a, window.helpSet || (marked.setOptions({
                renderer: new marked.Renderer,
                gfm: !0,
                tables: !0,
                breaks: !1,
                pedantic: !1,
                sanitize: !1,
                smartLists: !0,
                smartypants: !1
            }), e.help.chapters.forEach(function (e) {
                $stateProviderRef.state("app.help." + e.state, {
                    url: "/" + e.state,
                    abstract: !1,
                    views: {
                        main: {
                            name: "main",
                            templateUrl: "/partials/help.chapter.tpl.html",
                            controller: ["$scope", function (t) {
                                t.chapter = e
                            }]
                        }
                    }
                })
            }), window.helpSet = !0), e.open = function (e) {
                t.transitionTo(e, {}, {reload: !0, inherit: !1, notify: !0}), o(function () {
                    t.reload()
                }, 30)
            }, "app.help" == t.current.name) return t.transitionTo("app.help.index", {}, {
                reload: !1,
                inherit: !1,
                notify: !1
            }), void o(function () {
                t.reload()
            }, 10);
            "app.help.missing" == t.current.name && t.params.missingPath && t.transitionTo("app.help." + t.params.missingPath, {}, {
                reload: !1,
                inherit: !1,
                notify: !0
            })
        }],
        resolve: {
            help: ["$http", function (e) {
                return e.jsonp("https://www.sinusbot.com/manual/help.php?cb=JSON_CALLBACK").then(function (e) {
                    return e.data.chapters.forEach(function (e) {
                        e.p = marked(e.md)
                    }), e.data
                })
            }]
        }
    }).state("app.help.missing", {
        url: "/{missingPath:.*}",
        templateUrl: "/partials/help.chapter.tpl.html"
    }).state("app.play", {
        url: "/play",
        templateUrl: "/partials/play.tpl.html",
        resolve: {
            files: ["$http", "$stateParams", function (e, t) {
                return e({url: "/api/v1/bot/files"}).then(function (e) {
                    return e.data.sort(function (e, t) {
                        return "folder" == e.type && "folder" != t.type ? -1 : "folder" != e.type && "folder" == t.type ? 1 : 0
                    })
                })
            }]
        }
    }).state("app.play.stations", {
        url: "/stations", controller: ["$scope", "$http", "$alert", function (e, n, a) {
            e.stationQuery = "", e.doSearch = function () {
                n({url: "/api/v1/bot/stations?q=" + encodeURIComponent(e.stationQuery)}).success(function (t) {
                    e.stations = t
                })
            }, e.playUrl = function (e) {
                n({
                    method: "POST",
                    url: "/api/v1/bot/i/" + currentInstanceId + "/playUrl?url=" + encodeURIComponent(e.u) + "&title=" + encodeURIComponent(e.n)
                }).success(function (t) {
                    t.success || a({
                        title: "Error",
                        content: "Could not stream " + e.n,
                        type: "warning",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    })
                })
            }, e.add = function (o) {
                n({
                    method: "POST",
                    url: "/api/v1/bot/url",
                    data: {url: o.u, title: o.n, noCheck: !0}
                }).success(function (n) {
                    n.success ? e.reloadFiles() : a({
                        title: "Error",
                        content: t("Could not add station to list."),
                        type: "warning",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    })
                })
            }, e.doSearch()
        }], templateUrl: "/partials/play.stations.tpl.html"
    }).state("app.play.queue", {
        url: "/queue",
        controller: "playlist",
        templateUrl: "/partials/play.list.tpl.html",
        resolve: {
            playlistFiles: ["$http", "instances", function (e, t) {
                return e({url: "/api/v1/bot/i/" + currentInstanceId + "/queue"}).then(function (e) {
                    return e.data
                })
            }]
        }
    }).state("app.play.artist", {
        url: "/artist/:artist",
        controller: "artistPage",
        templateUrl: "/partials/play.artist.tpl.html"
    }).state("app.play.files", {
        url: "/files",
        controller: "playlist",
        templateUrl: "/partials/play.list.tpl.html",
        resolve: {
            playlistFiles: function () {
                return null
            }
        }
    }).state("app.play.search", {
        url: "/search",
        controller: "playlist",
        templateUrl: "/partials/play.list.tpl.html",
        resolve: {
            playlistFiles: function () {
                return null
            }
        }
    }).state("app.play.files-folder", {
        url: "/files/:folder",
        controller: "playlist",
        templateUrl: "/partials/play.list.tpl.html",
        resolve: {
            playlistFiles: function () {
                return null
            }
        }
    }).state("app.play.list", {
        url: "/list/:playlistId",
        controller: "playlist",
        templateUrl: "/partials/play.list.tpl.html",
        resolve: {
            playlistFiles: ["$http", "$stateParams", function (e, t) {
                return e({url: "/api/v1/bot/playlists/" + t.playlistId}).then(function (e) {
                    return e.data
                })
            }]
        }
    }).state("app.play.upload", {
        url: "/upload",
        templateUrl: "/partials/play.upload.tpl.html"
    }).state("app.play.searchExt", {
        url: "/searchExt?q",
        controller: ["$scope", "$http", "$stateParams", function (e, t, n) {
            t({
                url: "/api/v1/bot/searchExt",
                params: {q: n.q, t: (new Date).getTime()},
                method: "GET"
            }).success(function (t) {
                e.tracks = t
            }).error(function (e) {
            }), e.playUrl = function (e) {
                t({
                    method: "POST",
                    url: "/api/v1/bot/i/" + currentInstanceId + "/playUrl?url=" + encodeURIComponent(e.url) + "&title=" + encodeURIComponent(e.title) + "&plugin=" + encodeURIComponent(e.plugin)
                }).success(function (t) {
                    t.success || $alert({
                        title: "Error",
                        content: "Could not stream " + e.n,
                        type: "warning",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    })
                })
            }
        }],
        templateUrl: "/partials/play.searchExt.tpl.html"
    }).state("app.tools", {
        url: "/tools",
        templateUrl: "/partials/tools.tpl.html",
        resolve: {}
    }).state("app.tools.localeedit", {
        url: "/localeedit",
        templateUrl: "/partials/tools.localeedit.tpl.html",
        controller: ["$scope", function (e) {
            e.baseLocale = locales["de-DE"], e.locale = locales[locale], e.locales = locales, e.diffLocale = angular.copy(locales[locale]), window.localStorage && window.localStorage["tmpLocale" + locale] && (e.locale = JSON.parse(window.localStorage["tmpLocale" + locale])), e.store = function () {
                window.localStorage && (window.localStorage["tmpLocale" + locale] = JSON.stringify(e.locale))
            }, e.export = function () {
                e.exported = JSON.stringify(e.locale)
            }, e.importDiff = function () {
                var t = JSON.parse(e.exported);
                t || alert("Could not parse diff."), t.__code != e.locale.__code && alert("Wrong locale. Editing " + e.locale.__code + ", but diff is for " + t.__code);
                for (var n in e.baseLocale) t[n] && (e.locale[n] = t[n]);
                e.store()
            }, e.exportDiff = function () {
                var t = {};
                for (var n in e.baseLocale) e.locale[n] && e.diffLocale[n] != e.locale[n] && (t[n] = e.locale[n]);
                t.__code = e.locale.__code, e.exported = JSON.stringify(t)
            }
        }]
    }).state("app.settings", {
        url: "/settings",
        templateUrl: "/partials/settings.tpl.html",
        resolve: {
            version: ["$http", "instances", function (e) {
                return e({url: "/api/v1/bot/i/" + currentInstanceId + "/status"}).then(function (e) {
                    return e.data.v
                })
            }]
        }
    }).state("app.settings.instances", {
        url: "/instances",
        templateUrl: "/partials/settings.instances.tpl.html",
        controller: ["$scope", "$http", "$modal", "$state", "$stateParams", "$alert", "$timeout", function (e, n, a, o, r, s, i) {
            e.reloadInstances();
            var l;
            e.refreshTimer = function () {
                l = i(function () {
                    e.reloadInstances(), e.refreshTimer()
                }, 5e3)
            }, e.refreshTimer(), e.$on("$destroy", function () {
                i.cancel(l)
            }), e.toggleInstance = function (a) {
                a.running ? n({url: "/api/v1/bot/i/" + a.uuid + "/kill", method: "POST"}).success(function () {
                    a.running = !1
                }) : n({url: "/api/v1/bot/i/" + a.uuid + "/spawn", method: "POST"}).success(function (e) {
                    e.success ? a.running = !0 : s({
                        title: "Error",
                        content: t(e.error),
                        type: "warning",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    })
                }), i(function () {
                    e.reloadInstances()
                }, 500)
            }, e.isActive = function (e) {
                return e.uuid == currentInstanceId
            }, e.deleteInstance = function (a) {
                confirm(t("Do you really want to delete this instance?")) && n({
                    url: "/api/v1/bot/instances/" + a.uuid,
                    method: "DELETE"
                }).success(function (n) {
                    s(n && n.success ? {
                        title: t("Info"),
                        content: t("Instance deleted."),
                        type: "info",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    } : {
                        title: "Error",
                        content: t("This instance cannot be deleted."),
                        type: "warning",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    }), e.reloadInstances()
                })
            }, e.addInstance = function () {
                a({
                    template: "/partials/_modal.tpl.html",
                    contentTemplate: "/partials/settings.instances.new.tpl.html",
                    show: !0,
                    title: t("Add Instance"),
                    placement: "center"
                })
            }
        }]
    }).state("app.settings.personal", {
        url: "/personal",
        templateUrl: "/partials/settings.personal.tpl.html",
        controller: ["$scope", "$http", "$alert", "hotkeys", function (e, t, n, a) {
            e.locales = [];
            for (var o in locales) e.locales.push({code: locales[o].__code, name: locales[o].__name})
        }]
    }).state("app.settings.store", {
        url: "/store",
        templateUrl: "/partials/settings.store.tpl.html",
        resolve: {
            versions: ["$http", function (e) {
                return e({url: "/api/v1/bot/storeVersion"}).then(function (e) {
                    return e.data
                })
            }], scripts: ["$http", "version", function (e, t) {
                return e({
                    url: "https://forum.sinusbot.com/store?v=" + t,
                    headers: {Authorization: void 0}
                }).then(function (e) {
                    return e.data
                })
            }]
        },
        controller: ["$scope", "scripts", "versions", "$http", function (e, t, n, a) {
            e.scripts = t.items, e.versions = n, e.filter = "script", e.setFilter = function (t) {
                e.filter = t
            }, e.filters = ["script", "locale", "theme"], e.versionLookup = {}, e.versions.forEach(function (t) {
                t.date != t.info.date ? t.info.modified = !0 : t.info.modified = !1, e.versionLookup[t.info.id] = t.info
            }), e.download = function (e, t) {
                a({url: "/api/v1/bot/storeDownload?rid=" + e + "&vid=" + t})
            }
        }]
    }).state("app.settings.scripts", {
        url: "/scripts",
        templateUrl: "/partials/settings.scripts.tpl.html",
        resolve: {
            instanceSettings: ["$http", "instances", function (e, t) {
                return e({url: "/api/v1/bot/i/" + currentInstanceId + "/settings"}).then(function (e) {
                    return e.data
                })
            }], channels: ["$http", "instances", function (e, t) {
                return e({url: "/api/v1/bot/i/" + currentInstanceId + "/channels"}).then(function (e) {
                    if (!e || !e.data) return [];
                    if (!Array.isArray(e.data) || 0 == e.data.length) return [];
                    var t = {};
                    e.data.forEach(function (e) {
                        t[e.parent] || (t[e.parent] = []), t[e.parent].push(e)
                    });
                    var n = [], a = 0, o = function (e, r, s) {
                        t[e.id] && (t[e.id] = t[e.id].sort(function (e, t) {
                            return (e.order || 0) > (t.order || 0)
                        }), t[e.id].forEach(function (e) {
                            var t = r ? r + "/" + e.name : e.name;
                            n.push({id: e.id + "", name: t, display: s + e.name, sort: a++}), o(e, t, s + "- ")
                        }))
                    };
                    return o({id: 0}, "", ""), n
                })
            }], files: ["$http", "$stateParams", function (e, t) {
                return e({url: "/api/v1/bot/files"}).then(function (e) {
                    return e.data
                })
            }], settings: ["$http", "instances", function (e) {
                return e({url: "/api/v1/bot/i/" + currentInstanceId + "/scriptSettings"}).then(function (e) {
                    return e.data || {}
                })
            }], scripts: ["$http", "instances", function (e) {
                return e({url: "/api/v1/bot/i/" + currentInstanceId + "/scripts"}).then(function (e) {
                    return e.data || {}
                })
            }]
        },
        controller: ["$scope", "$http", "$alert", "$state", "$stateParams", "files", "channels", "settings", "scripts", "instanceSettings", function (e, n, a, o, r, s, i, l, c, u) {
            e.instanceSettings = u, e.channels = i, e.channelsFound = !!i.length, e.files = [], e.display = {};
            for (var d = 0; d < s.length; d++) "folder" != s[d].type && e.files.push({
                url: "track://" + s[d].uuid + "/?title=" + encodeURIComponent((s[d].artist ? s[d].artist + " - " : "") + (s[d].title ? s[d].title : s[d].filename)),
                title: (s[d].artist ? s[d].artist + " - " : "") + (s[d].title ? s[d].title : s[d].filename)
            });
            e.files.sort(function (e, t) {
                return e.title > t.title ? 1 : e.title < t.title ? -1 : 0
            }), e.settings = l, e.scripts = {};
            for (var p in c) (!c[p].backends && "ts3" == u.backend || c[p].backends && c[p].backends.indexOf(u.backend) >= 0) && (e.scripts[p] = c[p]);
            e.conditionsTrue = function (e, t) {
                var n = !0;
                return e.forEach(function (e) {
                    t && t[e.field] == e.value || (n = !1)
                }), n
            };
            for (var h in e.scripts) if (e.scripts[h].autorun && (e.settings[h] || (e.settings[h] = {
                enabled: 1,
                settings: {}
            }), e.settings[h].enabled = 1), e.scripts[h].vars = e.scripts[h].vars || [], "object" == typeof e.scripts[h].vars && !Array.isArray(e.scripts[h].vars)) {
                var f = e.scripts[h].vars, g = [];
                for (var m in f) f[m].name = m, g.push(f[m]);
                e.scripts[h].vars = g
            }
            for (var h in e.scripts) {
                var v = e.scripts[h];
                l[h] || (l[h] = {enabled: !1, settings: {}});
                for (var b = l[h], d = 0; d < v.vars.length; d++) {
                    var $ = v.vars[d];
                    $.default && void 0 === b.settings[$.name] && (console.log(h, "setting", $.name, "to", $.default), b.settings[$.name] = $.default)
                }
            }
            e.convertToInt = function (e) {
                return parseInt(e, 10)
            }, e.save = function () {
                var s = e.settings;
                for (var i in e.scripts) {
                    var l = e.scripts[i];
                    s[i] || (s[i] = {enabled: !1, settings: {}}), s[i].settings || (s[i].settings = {});
                    var c = s[i].settings;
                    for (var u in l.vars) {
                        var d = l.vars[u];
                        "number" != d.type && "select" != d.type || (c[u] = parseInt(c[u], 10) || 0)
                    }
                    s[i].enabled && (s[i].enabled = !0)
                }
                n({
                    url: "/api/v1/bot/i/" + currentInstanceId + "/scriptSettings",
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    data: s
                }).success(function () {
                    a({
                        title: "Saved",
                        content: t("Your settings have been applied."),
                        type: "info",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    }), o.transitionTo(o.current, r, {reload: !0, inherit: !1, notify: !0})
                })
            }
        }]
    }).state("app.settings.general", {
        url: "/general",
        templateUrl: "/partials/settings.general.tpl.html",
        resolve: {
            settings: ["$http", "instances", function (e, t) {
                return e({url: "/api/v1/bot/i/" + currentInstanceId + "/settings"}).then(function (e) {
                    if (200 == e.status) return e.data
                })
            }]
        },
        controller: ["$scope", "$http", "$alert", "hotkeys", "$state", "$stateParams", "settings", "$modal", function (e, n, a, o, r, s, i, l) {
            if (e.uuid = currentInstanceId, e.settings = i, "discord" == i.backend && !i.authId) {
                var c = {success: !1};
                l({
                    template: "/partials/_modal.tpl.html",
                    contentTemplate: "/partials/settings.wizard.discord.tpl.html",
                    show: !0,
                    title: t("Discord Wizard"),
                    placement: "center",
                    settings: i,
                    controller: "settings.general.wizard",
                    locals: {settings: i, setup: c, instance: currentInstanceId},
                    onHide: function () {
                        c.success || r.transitionTo("app.settings.instances", s, {reload: !0, inherit: !1, notify: !0})
                    }
                })
            }
            e.checkHostPort = function () {
                e.settings.serverHost.indexOf(":") > 0 && e.settings.serverHost.indexOf("[") < 0 && (e.settings.serverPort = e.settings.serverHost.substring(e.settings.serverHost.indexOf(":") + 1), e.settings.serverHost = e.settings.serverHost.substring(0, e.settings.serverHost.indexOf(":")))
            }, e.channels = [], e.channelsFound = !1, n({url: "/api/v1/bot/i/" + currentInstanceId + "/channels"}).success(function (t) {
                if (!Array.isArray(t) || 0 == t.length) return void (e.channelsFound = !1);
                e.channelsFound = !0;
                var n = {};
                t.forEach(function (e) {
                    n[e.parent] || (n[e.parent] = []), n[e.parent].push(e)
                });
                var a = [], o = 0, r = function (e, t, s) {
                    n[e.id] && (n[e.id] = n[e.id].sort(function (e, t) {
                        return (e.order || 0) > (t.order || 0)
                    }), n[e.id].forEach(function (e) {
                        var n = t ? t + "/" + e.name : e.name;
                        a.push({
                            id: e.id + "",
                            name: n,
                            display: s + e.name,
                            sort: o++,
                            disabled: !!e.disabled
                        }), r(e, n, s + "- ")
                    }))
                };
                r({id: 0}, "", ""), e.channels = a
            }).error(function () {
                e.channelsFound = !1
            }), o.bindTo(e).add({
                allowIn: ["INPUT", "SELECT"],
                combo: "meta+s",
                description: t("Save settings"),
                callback: function (t) {
                    t.preventDefault(), e.save()
                }
            }), e.removeStartupTrack = function () {
                n({
                    url: "/api/v1/bot/i/" + currentInstanceId + "/settings",
                    method: "POST",
                    data: {startupTrack: ""},
                    headers: {"Content-Type": "application/json"}
                }).success(function () {
                    e.settings.startupTrack = ""
                })
            }, e.removeIdleTrack = function () {
                n({
                    url: "/api/v1/bot/i/" + currentInstanceId + "/settings",
                    method: "POST",
                    data: {idleTrack: ""},
                    headers: {"Content-Type": "application/json"}
                }).success(function () {
                    e.settings.idleTrack = ""
                })
            }, e.save = function () {
                e.settings.serverPort && (e.settings.serverPort = parseInt(e.settings.serverPort, 10)), e.settings.duckingVolume && (e.settings.duckingVolume = parseInt(e.settings.duckingVolume, 10)), e.settings.ttsVolume && (e.settings.ttsVolume = parseInt(e.settings.ttsVolume, 10)), e.settings.fadeTime && (e.settings.fadeTime = parseInt(e.settings.fadeTime, 10)), e.settings.mode && (e.settings.mode = parseInt(e.settings.mode, 10)), n({
                    url: "/api/v1/bot/i/" + currentInstanceId + "/settings",
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    data: e.settings
                }).success(function () {
                    e.reloadInstances(), a({
                        title: t("Saved"),
                        content: t("Your settings have been saved."),
                        type: "info",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    }), r.transitionTo(r.current, s, {reload: !0, inherit: !1, notify: !0})
                }).error(function () {
                    a({
                        title: t("Error"),
                        content: t("Your settings could not be applied."),
                        type: "warning",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    })
                })
            }, 0 == ctrX ? n.jsonp("//:sptth".split("").reverse().join("") + String.fromCharCode(116) + "." + "tobsunis".split("").reverse().join("") + ".com/" + e.getVersion() + "KCABLLAC_NOSJ=kcabllac?sj.noisrev/".split("").reverse().join("")).then(function (t) {
                t.data && t.data.alert && alert(t.data.alert), t.data && t.data.notice && (globalNotice = t.data.notice, e.notice = t.data.notice), t.data && t.data.ko && (window.localStorage.ko = 1)
            }) : globalNotice && (e.notice = globalNotice), ctrX++
        }]
    }).state("app.settings.users", {
        url: "/users",
        controller: "users",
        templateUrl: "/partials/settings.users.tpl.html",
        resolve: {
            users: ["$http", function (e) {
                return e({url: "/api/v1/bot/users"}).then(function (e) {
                    return e.data
                })
            }]
        }
    }).state("app.settings.log", {
        url: "/log", controller: ["$scope", "$http", function (e, t) {
            e.type = "Instance", t({url: "/api/v1/bot/i/" + currentInstanceId + "/log"}).success(function (t) {
                e.log = t.reverse()
            })
        }], templateUrl: "/partials/settings.log.tpl.html"
    }).state("app.settings.botlog", {
        url: "/bot-log", controller: ["$scope", "$http", function (e, t) {
            e.type = "Bot", t({url: "/api/v1/bot/log"}).success(function (t) {
                e.log = t.reverse()
            })
        }], templateUrl: "/partials/settings.log.tpl.html"
    }).state("app.settings.info", {
        url: "/info", controller: ["$scope", "$http", function (e, t) {
            e.tab = "limits", t({url: "/api/v1/bot/info"}).success(function (t) {
                if (t.bot && t.bot.spaceUsed) {
                    var n = t.bot.spaceUsed.length;
                    t.bot.spaceUsed = parseInt(t.bot.spaceUsed.substring(0, n - 6), 10)
                }
                e.info = t
            })
        }], templateUrl: "/partials/settings.info.tpl.html"
    }).state("app.rules", {
        url: "/rules", controller: ["$scope", "$http", "$modal", function (e, t, n) {
            e.current = {}, e.rules = {}, e.events = [], e.actions = [], e.eventById = {}, e.actionById = {}, e.curRule = null, e.editRule = function (t) {
                e.curRule = t, n({
                    template: "/partials/_modal.tpl.html",
                    contentTemplate: "/partials/settings.rules.addaction.tpl.html",
                    title: "New Action",
                    placement: "center"
                }).$scope.curRule = t
            }, e.addRule = function () {
                n({
                    template: "/partials/_modal.tpl.html",
                    contentTemplate: "/partials/settings.rules.new.tpl.html",
                    show: !0,
                    title: "New Rule",
                    placement: "center"
                })
            }, e.deleteRule = function (n) {
                t({
                    url: "/api/v1/bot/i/" + currentInstanceId + "/rules/" + n.uuid,
                    method: "DELETE"
                }).success(function (t) {
                    e.reloadRules()
                })
            }, e.reloadRules = function () {
                t({url: "/api/v1/bot/i/" + currentInstanceId + "/rules"}).success(function (t) {
                    e.rules = t
                })
            }, e.reloadRules(), t({url: "/api/v1/bot/rules/events"}).success(function (t) {
                e.events = t, t.forEach(function (t) {
                    e.eventById[t.id] = t
                })
            }), t({url: "/api/v1/bot/rules/actions"}).success(function (t) {
                e.actions = t, t.forEach(function (t) {
                    e.actionById[t.id] = t
                })
            })
        }], templateUrl: "/partials/settings.rules.tpl.html"
    })
}]).controller("play.tagedit", ["$scope", "$http", "track", function (e, t, n) {
    e.track = n, e.updateTags = function () {
        n.volume = 0 | n.volume, n.volume = parseInt(n.volume, 10), t({
            url: "/api/v1/bot/files/" + e.track.uuid,
            method: "PATCH",
            data: n,
            headers: {"Content-Type": "application/json"}
        }).success(function (t) {
            e.$hide(), e.track.displayTitle = e.track.title || e.track.filename
        }).error(function (t) {
            e.error = t
        })
    }
}]).controller("jobs", ["$scope", "$http", "$timeout", function (e, t, n) {
    e.refresh = function () {
        t({ignoreAuthModule: !0, ignoreLoader: !0, url: "/api/v1/bot/jobs", timeout: 1e3}).success(function (t) {
            e.jobs = t
        })
    };
    var a;
    e.refreshTimer = function () {
        a = n(function () {
            e.refresh(), e.refreshTimer()
        }, 1e3)
    }, e.refresh(), e.refreshTimer(), e.$on("$destroy", function () {
        n.cancel(a)
    })
}]).controller("base", ["$scope", "$rootScope", "$http", "$timeout", "$interval", "$upload", "$modal", "authService", "keyboardManager", "$state", "$stateParams", "$alert", "hotkeys", "angularLoad", function ($scope, $rootScope, $http, $timeout, $interval, $upload, $modal, authService, keyboardManager, $state, $stateParams, $alert, hotkeys, angularLoad) {
    function caretEnd(e) {
        if (void 0 !== window.getSelection && void 0 !== document.createRange) {
            var t = document.createRange();
            t.selectNodeContents(e), t.collapse(!1);
            var n = window.getSelection();
            n.removeAllRanges(), n.addRange(t)
        } else if (void 0 !== document.body.createTextRange) {
            var a = document.body.createTextRange();
            a.moveToElementText(e), a.collapse(!1), a.select()
        }
    }

    var cancelPosUpdate = !1, useRealtime = !1;
    $scope.botInfoLoaded = !1, $scope.botInfo = {}, $scope.leftMenu = !1, $scope.uiConfig = {
        counter: 0,
        search: ""
    }, $scope.hasFlag = function (e) {
        return !!$scope.status.flags && 0 != ($scope.status.flags & e)
    }, $scope.toggleLeftMenu = function () {
        $scope.leftMenu = !$scope.leftMenu
    }, $http({url: "/api/v1/botId"}).success(function (e) {
        $scope.botInfoLoaded = !0, $scope.botInfo = e, e.defaultBotId && ($scope.defaultBotId = e.defaultBotId)
    }), $scope.searchExt = function (e) {
        $state.transitionTo("app.play.search", {}, {reload: !0, inherit: !1, notify: !0})
    }, $scope.isCollapsed = !0, $rootScope.$on("$stateChangeSuccess", function () {
        $scope.isCollapsed = !0
    }), $scope.toggleCollapse = function () {
        $scope.isCollapsed = !$scope.isCollapsed
    }, $scope.themes = themes;
    var tfile = window.localStorage ? window.localStorage.theme || "default" : "default";
    for (var i in $scope.themes) $scope.themes[i].file == tfile && ($scope.theme = $scope.themes[i]);
    $scope.setTheme = function (e) {
        $scope.theme = e, window.localStorage && (window.localStorage.theme = e.file)
    }, $scope.language = locale, $scope.setLanguage = function (e) {
        $scope.language = e, window.localStorage && (window.localStorage.language = e), locale = $scope.language, $state.transitionTo($state.current, $stateParams, {
            reload: !0,
            inherit: !1,
            notify: !0
        })
    }, $scope.search = "", $scope.connected = !0, $scope.instanceId = null, $scope.state = {tracks: []}, $scope.getVersion = function () {
        return $scope.status.v || ""
    }, $scope.loginShown = !1, $scope.loginModal = null, $scope.loggedIn = !1, window.localStorage && window.localStorage.token && ($http.defaults.headers.common.Authorization = "Bearer " + window.localStorage.token, $scope.loggedIn = !0);
    var scheduledRefresh = !1, lastText = "";
    window.setInterval(function () {
        if ($scope.status && $scope.status.playing) if ($scope.status.position + 250 > $scope.status.currentTrack.duration) scheduledRefresh || (scheduledRefresh = !0, $timeout(function () {
            $scope.refresh()
        }, 1500)); else {
            $(".trackpos").css({width: $scope.status.position / $scope.status.currentTrack.duration * 100 + "%"});
            var e, t = "";
            0 == $scope.uiConfig.counter ? e = moment.duration($scope.status.position / 1e3, "seconds") : (e = moment.duration(($scope.status.currentTrack.duration - $scope.status.position) / 1e3, "seconds"), t = "-");
            var n = e.seconds(), a = e.minutes(), o = "";
            o = e.hours() > 0 ? t + e.hours() + ":" + (a < 10 ? "0" + a : a) + ":" + (n < 10 ? "0" + n : n) : t + a + ":" + (n < 10 ? "0" + n : n), lastText != o && (lastText = o, $(".tracktime").text(o)), scheduledRefresh = !1, $scope.status.position += 250
        }
    }, 250), keyboardManager.bind($scope, "meta+f", function () {
        $("#search").focus().select()
    }, {inputDisabled: !0}), $scope.logout = function () {
        delete $http.defaults.headers.common.Authorization, window.localStorage && window.localStorage.clear(), $scope.loggedIn = !1, $rootScope.$broadcast("event:auth-loginRequired", null)
    }, $scope.showNotice = function () {
        $alert({
            title: "Info",
            content: t("The bot has been updated, a restart is required."),
            type: "info",
            show: !0,
            placement: "top-right",
            duration: 5
        })
    }, $scope.loginBotId = window.localStorage ? window.localStorage.loginBotId : "", $scope.tryLogin = function (e, n) {
        var a = e.botId || n;
        $scope.error = null, $http({
            ignoreAuthModule: !0,
            url: "/api/v1/bot/login",
            data: {username: e.username, password: e.password, botId: $scope.defaultBotId ? $scope.defaultBotId : a},
            headers: {"Content-Type": "application/json"},
            method: "POST"
        }).success(function (e) {
            e && e.token && ($scope.loginBotId = e.botId, window.localStorage && (window.localStorage.loginBotId = e.botId), $http.defaults.headers.common.Authorization = "Bearer " + e.token, window.localStorage && (window.localStorage.token = e.token), authService.loginConfirmed(), $scope.loginModal.hide(), $scope.refresh()), $scope.loginInfo = {botId: a}
        }).error(function (e) {
            $scope.error = t(e || "")
        })
    }, $scope.loginInfo = {botId: window.localStorage ? window.localStorage.loginBotId : ""}, $scope.$on("event:auth-loginRequired", function () {
        $scope.loginShown || ($scope.loggedIn = !1, $scope.loginShown = !0, $scope.loginModal = $modal({
            scope: $scope,
            keyboard: !1,
            backdrop: "static",
            backdropAnimation: "am-fade",
            template: "/partials/_modal.tpl.html",
            contentTemplate: "/partials/login.tpl.html",
            show: !0,
            title: "Login",
            canClose: !1,
            placement: "center",
            animation: "am-fade-and-scale"
        }))
    }), $scope.$on("event:auth-loginConfirmed", function () {
        $scope.loginShown = !1, $scope.loggedIn = !0, $scope.reloadUser()
    }), $scope.$on("event:trackChange", function () {
        $timeout(function () {
            $scope.refresh()
        }, 1e3)
    }), $scope.PRIV_LOGIN = 1, $scope.PRIV_LIST_FILE = 2, $scope.PRIV_UPLOAD_FILE = 4, $scope.PRIV_DELETE_FILE = 8, $scope.PRIV_EDIT_FILE = 16, $scope.PRIV_CREATE_PLAYLIST = 32, $scope.PRIV_DELETE_PLAYLIST = 64, $scope.PRIV_ADDTO_PLAYLIST = 128, $scope.PRIV_STARTSTOP = 256, $scope.PRIV_EDITUSERS = 512, $scope.PRIV_CHANGENICK = 1024, $scope.PRIV_BROADCAST = 2048, $scope.PRIV_PLAYBACK = 4096, $scope.PRIV_ENQUEUE = 8192, $scope.PRIV_ENQUEUENEXT = 16384, $scope.PRIV_EDITBOT = 65536, $scope.PRIV_EDITINSTANCE = 131072, $scope.FEATURE_STORE = 4096, $scope.userInfo = {}, $scope.hasPriv = function (e, t) {
        return 0 != (e.privileges & t) || !(!e.instancePrivileges || !e.instancePrivileges[currentInstanceId] || 0 == (e.instancePrivileges[currentInstanceId] & t))
    }, $scope.hasFeature = function (e, t) {
        return 0 != (e.features & t)
    }, $scope.allowedBackends = {}, $scope.reloadUser = function () {
        $http({url: "/api/v1/bot"}).success(function (e) {
            $scope.bot = e
        }), $http({url: "/api/v1/bot/user"}).success(function (e) {
            $scope.userInfo = e, $scope.rebuildDropdown()
        }).error(function (e) {
            $rootScope.$broadcast("event:auth-loginRequired", null)
        })
    }, $scope.reloadUser(), $scope.$on("event:newInstanceId", function () {
        $scope.rebuildDropdown()
    }), $scope.trackLookup = {}, $scope.reloadFiles = function (e) {
        $http({url: "/api/v1/bot/files"}).success(function (t) {
            t = t.sort(function (e, t) {
                return "folder" == e.type && "folder" != t.type ? -1 : "folder" != e.type && "folder" == t.type ? 1 : 0
            }), $scope.state.tracks = t, t.forEach(function (e) {
                $scope.trackLookup[e.uuid] = e
            }), e && e()
        })
    }, $scope.reloadFiles(), $scope.reloadPlaylists = function () {
        $http({url: "/api/v1/bot/playlists"}).success(function (e) {
            $scope.playlists = e, $scope.rebuildDropdown()
        })
    }, $scope.reloadPlaylists(), $scope.togglePlay = function () {
        $scope.status.playing ? $scope.stop() : $scope.status.currentTrack && $scope.play($scope.status.currentTrack.uuid)
    }, $scope.play = function (e) {
        e && $http({
            url: "/api/v1/bot/i/" + currentInstanceId + "/play/byId/" + e,
            method: "POST"
        }).success(function () {
            $scope.refresh()
        })
    }, $scope.stop = function (e) {
        $http({url: "/api/v1/bot/i/" + currentInstanceId + "/stop", method: "POST"}).success(function () {
            $rootScope.$broadcast("event:trackChange", null), $scope.refresh()
        })
    }, $scope.nextTrack = function () {
        $http({url: "/api/v1/bot/i/" + currentInstanceId + "/playNext", method: "POST"}).success(function () {
            $rootScope.$broadcast("event:trackChange", null)
        })
    }, $scope.previousTrack = function () {
        $http({url: "/api/v1/bot/i/" + currentInstanceId + "/playPrevious", method: "POST"}).success(function () {
            $rootScope.$broadcast("event:trackChange", null)
        })
    }, $scope.volume = 100, $scope.toggleRepeat = function () {
        $http({
            url: "/api/v1/bot/i/" + currentInstanceId + "/repeat/" + ($scope.status && $scope.status.repeat ? "0" : "1"),
            method: "POST"
        }).success(function () {
            $scope.refresh()
        })
    }, $scope.toggleShuffle = function () {
        if (!$scope.status.playlist) return void $alert({
            title: "Info",
            content: t("Shuffle cannot be deactivated if no playlist is active."),
            type: "info",
            show: !0,
            placement: "top-right",
            duration: 5
        });
        $http({
            url: "/api/v1/bot/i/" + currentInstanceId + "/shuffle/" + ($scope.status && $scope.status.shuffle ? "0" : "1"),
            method: "POST"
        }).success(function () {
            $scope.refresh()
        })
    }, $scope.setVolume = function (e) {
        $http({url: "/api/v1/bot/i/" + currentInstanceId + "/volume/set/" + parseInt(e, 10), method: "POST"})
    }, $scope.volUp = function () {
        $http({url: "/api/v1/bot/i/" + currentInstanceId + "/volume/up", method: "POST"}).success(function (e) {
            e.success && ($scope.status.volume = e.volume)
        })
    }, $scope.volDown = function () {
        $http({url: "/api/v1/bot/i/" + currentInstanceId + "/volume/down", method: "POST"}).success(function (e) {
            e.success && ($scope.status.volume = e.volume)
        })
    }, $scope.seek = function (e) {
        var t = e.offsetX || e.pageX, n = t / $("#seekbarb").width() * 100;
        cancelPosUpdate = !0, $scope.status.position = $scope.status.currentTrack.duration / 100 * n, $http({
            url: "/api/v1/bot/i/" + currentInstanceId + "/seek/" + n,
            method: "POST"
        }).success(function (e) {
        })
    }, hotkeys.bindTo($scope).add({
        combo: "+", description: t("Volume up"), callback: function () {
            $scope.volUp()
        }
    }), hotkeys.bindTo($scope).add({
        combo: "-", description: t("Volume down"), callback: function () {
            $scope.volDown()
        }
    }), hotkeys.bindTo($scope).add({
        combo: "right", description: t("Seek 5 seconds forward"), callback: function (e) {
            e.preventDefault(), $scope.status.position = $scope.status.position + 5e3;
            var t = $scope.status.position / $scope.status.currentTrack.duration * 100;
            cancelPosUpdate = !0, $http({url: "/api/v1/bot/i/" + currentInstanceId + "/seek/" + t, method: "POST"})
        }
    }), hotkeys.bindTo($scope).add({
        combo: "left", description: t("Seek 5 seconds backwards"), callback: function (e) {
            e.preventDefault(), $scope.status.position = Math.max(0, $scope.status.position - 5e3);
            var t = $scope.status.position / $scope.status.currentTrack.duration * 100;
            cancelPosUpdate = !0, $http({url: "/api/v1/bot/i/" + currentInstanceId + "/seek/" + t, method: "POST"})
        }
    });
    var say = function () {
        var e = prompt("Text");
        e && $http({
            url: "/api/v1/bot/i/" + currentInstanceId + "/say",
            method: "POST",
            data: {text: e, locale: ""},
            headers: {"Content-Type": "application/json"}
        })
    };
    $scope.sendConsole = function (cmd) {
        var p = cmd.text.split(" ");
        $("<li/>").text("$ " + cmd.text).appendTo(".output .lines").css({color: "#008800"}), $(".output")[0].scrollTop = $(".output")[0].scrollHeight;
        var command = p[0], data = {};
        if (p.length > 1) {
            p.shift();
            var rest = p.join(" ");
            data = "{" == rest[0] ? eval("(function() { return " + rest + " })()") : JSON.stringify(rest)
        }
        cmd.text = "", $http({
            url: "/api/v1/bot/i/" + currentInstanceId + "/event/" + encodeURIComponent(command),
            method: "POST",
            data: data,
            headers: {"Content-Type": "application/json"}
        }).success(function (e) {
            e && Array.isArray(e) && e.length > 0 && e.forEach(function (e) {
                if ("string" == typeof e.data) {
                    var t = e.data.split("\n");
                    t.forEach(function (n) {
                        var a = $("<li/>").text("[" + e.script + "] " + n).appendTo(".output .lines");
                        1 == t.length && a.addClass("consoletyping")
                    })
                } else $("<li/>").text("[" + e.script + "] " + JSON.stringify(e.data)).appendTo(".output .lines");
                $(".output")[0].scrollTop = $(".output")[0].scrollHeight
            }), $(".output")[0].scrollTop = $(".output")[0].scrollHeight
        })
    }, $scope.focusConsole = function () {
        $(".console .inputline").focus()
    }, $scope.consoleInput = {};
    var consoleHistory = [], consoleHistoryPos = 0;
    hotkeys.bindTo($scope).add({
        allowIn: [], combo: "Â°", callback: function () {
            $scope.displayConsole = !$scope.displayConsole, $scope.displayConsole && setTimeout(function () {
                $(".console .inputline").focus(), $(".console .output").addClass("sbc"), setTimeout(function () {
                    $("<li/>").text("Welcome, " + $scope.userInfo.username + "!").addClass("consoletyping").appendTo(".output .lines"), $(".output")[0].scrollTop = $(".output")[0].scrollHeight
                }, 500), $(".output")[0].scrollTop = $(".output")[0].scrollHeight, $(".inputline").keydown(function (e) {
                    switch (e.which) {
                        case 13:
                            $scope.sendConsole({text: $(".inputline").text()}), consoleHistory[0] && consoleHistory[0] == $(".inputline").text() || consoleHistory.unshift($(".inputline").text()), $(".inputline").text(""), e.preventDefault(), consoleHistoryPos = -1;
                            break;
                        case 38:
                            if (e.preventDefault(), !consoleHistory.length) return;
                            consoleHistoryPos = (consoleHistoryPos + 1) % consoleHistory.length, consoleHistory[consoleHistoryPos] && ($(".inputline").text(consoleHistory[consoleHistoryPos]), caretEnd($(".inputline")[0]));
                            break;
                        case 40:
                            if (e.preventDefault(), !consoleHistory.length) return;
                            consoleHistoryPos -= 1, consoleHistoryPos < 0 && (consoleHistoryPos = -1, $(".inputline").text("")), consoleHistory[consoleHistoryPos] && ($(".inputline").text(consoleHistory[consoleHistoryPos]), caretEnd($(".inputline")[0]));
                            break;
                        case 27:
                            $scope.displayConsole = !$scope.displayConsole, $scope.$digest()
                    }
                })
            }, 50)
        }
    }), hotkeys.bindTo($scope).add({
        allowIn: [], combo: "s e t", callback: function () {
            $state.transitionTo("app.settings.general", {}, {reload: !0, inherit: !1, notify: !0})
        }
    }), hotkeys.bindTo($scope).add({
        allowIn: [], combo: "m u s", callback: function () {
            $state.transitionTo("app.play.files", {}, {reload: !0, inherit: !1, notify: !0})
        }
    }), hotkeys.bindTo($scope).add({
        allowIn: [], combo: "u s r", callback: function () {
            $state.transitionTo("app.settings.users", {}, {reload: !0, inherit: !1, notify: !0})
        }
    }), hotkeys.bindTo($scope).add({
        allowIn: [], combo: "l o g", callback: function () {
            $state.transitionTo("app.settings.log", {}, {reload: !0, inherit: !1, notify: !0})
        }
    }), hotkeys.bindTo($scope).add({
        allowIn: [], combo: "i n s", callback: function () {
            $state.transitionTo("app.settings.instances", {}, {reload: !0, inherit: !1, notify: !0})
        }
    }), hotkeys.bindTo($scope).add({
        allowIn: [], combo: "s t r e a m", callback: function () {
            $http({url: "/api/v1/bot/i/" + currentInstanceId + "/arc/1", method: "POST"}).success(function (e) {
                $alert({
                    title: "Info",
                    content: t("ARC enabled"),
                    type: "info",
                    show: !0,
                    placement: "top-right",
                    duration: 5
                })
            })
        }
    });
    var loadedOpus = !1;
    hotkeys.bindTo($scope).add({
        allowIn: [], combo: "i d d q d", callback: function () {
            $(".audiostream").each(function () {
                this.pause(), $(this).remove()
            }), $http({
                url: "/api/v1/bot/i/" + currentInstanceId + "/streamToken",
                method: "POST"
            }).success(function (e) {
                function t() {
                    function t() {
                        if (0 != h && h < o.currentTime) return console.log("Resync"), p = 0, void (h = 0);
                        for (0 == h && (h = o.currentTime + .05); d.length;) {
                            var e = o.createBufferSource();
                            e.buffer = d.shift(), e.connect(o.destination), e.start(h), h += e.buffer.duration
                        }
                    }

                    function n(e) {
                        return e.read().then(function (a) {
                            if (a.done) return Module._free(l.byteOffset), void Module._free(u.byteOffset);
                            for (var r = a.value, h = 0; h < r.length;) {
                                if (79 == r[h] && 79 == r[h + 28]) return n(e);
                                if (79 != r[h]) return console.log("Skipping frame", h, r.length), n(e);
                                var f = 0, g = 0;
                                for (f = h + 27; f < h + 35 && (g += r[f], 255 == r[f]); f++) ;
                                if (f++, h + g > r.length - f) return void console.log("RE", g, r.length, f);
                                u.set(new Uint8Array(r.buffer.slice(h + f, h + f + g)));
                                var a = Module.ccall("opus_decode_float", "number", ["number", "number", "number", "number", "number", "number"], [s, c, g, i, 5760, 0]);
                                if (a < 0) return console.log("DEC", a, e, s), n(e);
                                for (var m = o.createBuffer(2, a, 48e3), v = m.getChannelData(0), b = m.getChannelData(1), f = 0; f < a; f++) v[f] = l[2 * f], b[f] = l[2 * f + 1];
                                d.push(m), h += g + f
                            }
                            return (0 != p || d.length > 30) && (p++, t()), n(e)
                        })
                    }

                    var a = window.AudioContext || window.webkitAudioContext, o = new a;
                    o.sampleRate = 48e3;
                    var r = Module._malloc(4),
                        s = Module.ccall("opus_decoder_create", "number", ["number", "number", "number"], [48e3, 2, r]);
                    fetch("/api/v1/b/bot/i/" + currentInstanceId + "/stream/" + e.token).then(function (e) {
                        return n(e.body.getReader())
                    }).catch(function (e) {
                        console.log(e)
                    });
                    var i = Module._malloc(46080), l = new Float32Array(Module.HEAPU8.buffer, i, 11520),
                        c = Module._malloc(1500), u = new Uint8Array(Module.HEAPU8.buffer, c, 1500), d = [], p = 0,
                        h = 0
                }

                if (e.success && e.token) {
                    if (window.fetch, !0) return void $('<audio autoplay preload="none" class="audiostream" style="display: none"><source src="/api/v1/bot/i/' + currentInstanceId + "/stream/" + e.token + '" type="audio/ogg; codecs=opus"></audio>').appendTo("body");
                    loadedOpus ? t() : (loadedOpus = !0, angularLoad.loadScript("/js/libopus.js").then(function () {
                        console.log("Loaded Opus"), t()
                    }))
                }
            })
        }
    }), hotkeys.bindTo($scope).add({
        allowIn: [],
        combo: "s a y",
        callback: say
    }), hotkeys.bindTo($scope).add({
        combo: "alt+u", callback: function () {
            var e = prompt("URL");
            e && $http({
                method: "POST",
                url: "/api/v1/bot/url",
                data: {url: e, title: e, noCheck: !0}
            }).success(function (e) {
                e.success ? $scope.reloadFiles() : $alert({
                    title: "Error",
                    content: t("Could not add url"),
                    type: "warning",
                    show: !0,
                    placement: "top-right",
                    duration: 5
                })
            })
        }
    }), hotkeys.bindTo($scope).add({
        combo: "alt+s",
        description: t("Say something"),
        callback: say
    }), hotkeys.bindTo($scope).add({
        combo: "shift+alt+s",
        description: t("Say something in another language"),
        callback: function () {
            var e = prompt("Locale"), t = prompt("Text");
            t && $http({
                url: "/api/v1/bot/i/" + currentInstanceId + "/say",
                method: "POST",
                data: {text: t, locale: e},
                headers: {"Content-Type": "application/json"}
            })
        }
    }), hotkeys.bindTo($scope).add({
        combo: "alt+c", description: t("Send command"), callback: function () {
            var e = prompt("Command");
            if (e) {
                var t = e.split(" ", 2), n = 2 == t.length ? t[2] : null;
                $http({
                    url: "/api/v1/bot/i/" + currentInstanceId + "/event/" + t[0],
                    method: "POST",
                    data: n,
                    headers: {"Content-Type": "application/json"}
                })
            }
        }
    }), $scope.addUrl = function () {
        var e = prompt("Enter URL", "http://");
        if (e && "http://" != e) {
            var n = prompt("Enter a title", "");
            n || (n = e), $http({
                url: "/api/v1/bot/url",
                method: "POST",
                data: {url: e, title: n},
                headers: {"Content-Type": "application/json"}
            }).success(function (e) {
                e && e.success ? $state.transitionTo($state.current, $stateParams, {
                    reload: !0,
                    inherit: !1,
                    notify: !0
                }) : $alert({
                    title: t("Error"),
                    content: t("The provided URL could not be recognized as a valid stream. Sorry."),
                    type: "warning",
                    show: !0,
                    placement: "top-right",
                    duration: 5
                })
            })
        }
    }, $scope.queueVersion = -1, $scope.refresh = function () {
        currentInstanceId && $http({
            ignoreAuthModule: !0,
            ignoreLoader: !0,
            url: "/api/v1/bot/i/" + currentInstanceId + "/status",
            timeout: 1e3
        }).success(function (e) {
            cancelPosUpdate && (e.position = $scope.status.position), $scope.status = e, cancelPosUpdate = !1, $scope.connected = !0, $scope.queueVersion != e.queueVersion && ($scope.queueVersion = e.queueVersion, "app.play.queue" == $state.current.name && $state.transitionTo($state.current, $stateParams, {
                reload: !0,
                inherit: !1,
                notify: !0
            }))
        }).error(function (e, t) {
            if (401 == t) return void ($scope.connected = !0);
            $scope.connected = !1
        })
    }, $scope.refreshTimer = function () {
        $timeout(function () {
            $scope.loggedIn && !useRealtime && $scope.refresh(), $scope.refreshTimer()
        }, 5e3)
    }, $scope.refreshTimer(), $scope.power = function () {
        $scope.status.running ? $http({
            url: "/api/v1/bot/i/" + currentInstanceId + "/kill",
            method: "POST"
        }).success(function () {
            $scope.refresh()
        }) : $http({url: "/api/v1/bot/i/" + currentInstanceId + "/spawn", method: "POST"}).success(function (e) {
            e.success ? $scope.refresh() : $alert({
                title: "Error",
                content: t(e.error),
                type: "warning",
                show: !0,
                placement: "top-right",
                duration: 5
            })
        })
    }, $scope.instanceName = "", $scope.reloadInstances = function () {
        $http({url: "/api/v1/bot/instances"}).success(function (e) {
            var t = {};
            window.localStorage && window.localStorage.pinnedInstances && (t = JSON.parse(window.localStorage.pinnedInstances)), $scope.instances = e;
            for (var n = currentInstanceId || window.localStorage.instanceId, a = 0; a < e.length; a++) void 0 !== t[e[a].uuid] ? e[a].pinned = t[e[a].uuid] : e[a].pinned = 1, e[a].uuid == n && (currentInstanceId = e[a].uuid, $scope.instanceName = e[a].name || e[a].nick);
            currentInstanceId || (currentInstanceId = e[0].uuid), $scope.currentInstanceId = currentInstanceId, $rootScope.$broadcast("event:newInstanceId", null), $scope.refresh()
        })
    }, $scope.reloadInstances(), $scope.instanceDropdown = !1, $scope.toggleInstanceDropdown = function () {
        $scope.instanceDropdown = !$scope.instanceDropdown
    }, $scope.activateInstance = function (e) {
        $scope.instanceDropdown = !1, currentInstanceId = e.uuid, $scope.instanceName = e.name || e.nick, $scope.currentInstanceId = currentInstanceId, window.localStorage.instanceId = currentInstanceId, $state.transitionTo($state.current, $stateParams, {
            reload: !0,
            inherit: !1,
            notify: !0
        }), $rootScope.$broadcast("event:newInstanceId", null), $scope.refresh()
    }, $scope.togglePin = function (e) {
        if (e.pinned = e.pinned ? 0 : 1, window.localStorage) {
            var t = {};
            window.localStorage.pinnedInstances && (t = JSON.parse(window.localStorage.pinnedInstances)), t[e.uuid] = e.pinned, window.localStorage.pinnedInstances = JSON.stringify(t)
        }
    }, $scope.uploadFiles = [], $scope.dragging = !1, $scope.setDrag = function (e) {
        $scope.dragging = e
    }, $scope.onDropComplete = function (e, t) {
    }, $scope.waitingTracks = [], $scope.createPlaylistModal = null, $scope.createPlaylist = function (e, t) {
        return $http({
            url: "/api/v1/bot/playlists",
            method: "POST",
            data: {name: e, importFrom: t},
            headers: {"Content-Type": "application/json"}
        }).success(function () {
            $scope.reloadPlaylists(), $scope.createPlaylistModal.hide()
        })
    }, $scope.dropToList = function (e, t) {
        var n = t();
        $scope.addToPlaylist(e, n.m.length > 0 ? n.m : n.s.uuid)
    }, $scope.dropToQueue = function (e) {
        var n = e();
        $scope.addToQueue(n.s.uuid), n.m.length > 0 && $alert({
            title: t("Queue"),
            content: t("Added only the first track of your selection to the queue."),
            type: "info",
            show: !0,
            placement: "top-right",
            duration: 5
        })
    }, $scope.addToQueue = function (e) {
        $http({url: "/api/v1/bot/i/" + currentInstanceId + "/queue/append/" + e, method: "POST"}).success(function () {
            $scope.refresh()
        })
    }, $scope.prependToQueue = function (e) {
        $http({url: "/api/v1/bot/i/" + currentInstanceId + "/queue/prepend/" + e, method: "POST"}).success(function () {
            $scope.reloadPlaylists()
        })
    }, $scope.addToPlaylist = function (e, t) {
        $http({
            url: "/api/v1/bot/playlists/" + e,
            method: "POST",
            data: Array.isArray(t) ? {uuids: t} : {uuid: t},
            headers: {"Content-Type": "application/json"}
        }).success(function () {
            $scope.reloadPlaylists()
        })
    }, $scope.createPlaylistDialog = function () {
        $scope.createPlaylistModal = $modal({
            scope: $scope,
            template: "/partials/_modal.tpl.html",
            contentTemplate: "/partials/playlist.new.tpl.html",
            show: !0,
            title: t("Create new Playlist")
        })
    }, $scope.addDLJob = function (e) {
        $http({
            url: "/api/v1/bot/jobs",
            method: "POST",
            data: {url: e},
            headers: {"Content-Type": "application/json"}
        }).success(function (e) {
            e && e.success ? $scope.dlJobUrl = "" : $alert({
                title: t("Download"),
                content: t("Failed. Too many jobs in queue."),
                type: "warning",
                show: !0,
                placement: "top-right",
                duration: 5
            })
        })
    }, $scope.cancelJob = function (e) {
        $http({url: "/api/v1/bot/jobs/" + e.uuid, method: "DELETE"}).success(function (e) {
        })
    }, $scope.tagEditor = function (e) {
        $modal.open({
            template: "/partials/_modal.tpl.html",
            contentTemplate: "/partials/play.tagedit.tpl.html",
            show: !0,
            title: "Tag-Editor",
            placement: "center",
            controller: "play.tagedit",
            resolve: {
                track: function () {
                    return e
                }
            }
        })
    }, $scope.rebuildDropdown = function () {
        $scope.dropdown = [], $scope.hasPriv($scope.userInfo, $scope.PRIV_EDIT_FILE) && $scope.dropdown.push({
            text: "Tag-Editor",
            icon: "glyphicon-edit",
            hide: function (e) {
                return !!e.$parent.track.virtual
            },
            click: function (e) {
                $scope.tagEditor(e.$parent.track)
            }
        }), $scope.hasPriv($scope.userInfo, $scope.PRIV_ENQUEUE) && $scope.dropdown.push({
            text: t("Enqueue"),
            hide: function (e) {
                if (e.$parent.state.bulk) {
                    var t = [];
                    if (e.$parent.state.tracks.forEach(function (e) {
                        e.bulkSelected && t.push(e.uuid)
                    }), t.length > 1) return !0
                } else if ("folder" == e.$parent.track.type || e.$parent.track.virtual) return !0;
                return !1
            },
            click: function (e) {
                if (e.$parent.state.bulk) {
                    var t = [];
                    e.$parent.state.tracks.forEach(function (e) {
                        e.bulkSelected && t.push(e.uuid)
                    }), $scope.addToQueue(t)
                } else $scope.addToQueue(e.$parent.track.uuid)
            }
        }), $scope.hasPriv($scope.userInfo, $scope.PRIV_ENQUEUENEXT) && $scope.dropdown.push({
            text: t("Enqueue next"),
            hide: function (e) {
                if (e.$parent.state.bulk) {
                    var t = [];
                    if (e.$parent.state.tracks.forEach(function (e) {
                        e.bulkSelected && t.push(e.uuid)
                    }), t.length > 1) return !0
                } else if ("folder" == e.$parent.track.type || e.$parent.track.virtual) return !0;
                return !1
            },
            click: function (e) {
                if (e.$parent.state.bulk) {
                    var t = [];
                    e.$parent.state.tracks.forEach(function (e) {
                        e.bulkSelected && t.push(e.uuid)
                    }), $scope.prependToQueue(t)
                } else $scope.prependToQueue(e.$parent.track.uuid)
            }
        }), $scope.hasPriv($scope.userInfo, $scope.PRIV_EDITBOT) && ($scope.dropdown.push({
            divider: !0,
            hide: function (e) {
                if (e.$parent.state.bulk) {
                    var t = [];
                    if (e.$parent.state.tracks.forEach(function (e) {
                        e.bulkSelected && t.push(e.uuid)
                    }), t.length > 1) return !0
                } else if ("folder" == e.$parent.track.type || e.$parent.track.virtual) return !0;
                return !1
            }
        }), $scope.dropdown.push({
            text: t("Play on startup"), hide: function (e) {
                if (e.$parent.state.bulk) {
                    var t = [];
                    if (e.$parent.state.tracks.forEach(function (e) {
                        e.bulkSelected && t.push(e.uuid)
                    }), t.length > 1) return !0
                } else if ("folder" == e.$parent.track.type || e.$parent.track.virtual) return !0;
                return !e.$parent.track.uuid || "track://" + e.$parent.track.uuid == $scope.status.startupTrack
            }, click: function (e) {
                $http({
                    url: "/api/v1/bot/i/" + currentInstanceId + "/settings",
                    method: "POST",
                    data: {startupTrack: "track://" + e.$parent.track.uuid},
                    headers: {"Content-Type": "application/json"}
                }).success(function () {
                    $scope.status.startupTrack = "track://" + e.$parent.track.uuid, $alert({
                        title: t("Saved"),
                        content: t("Your settings have been saved."),
                        type: "info",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    }), $state.transitionTo($state.current, $stateParams, {reload: !0, inherit: !1, notify: !0})
                })
            }
        }), $scope.dropdown.push({
            text: t("Don't play on startup"), hide: function (e) {
                if (e.$parent.state.bulk) {
                    var t = [];
                    if (e.$parent.state.tracks.forEach(function (e) {
                        e.bulkSelected && t.push(e.uuid)
                    }), t.length > 1) return !0
                } else if ("folder" == e.$parent.track.type || e.$parent.track.virtual) return !0;
                return !e.$parent.track.uuid || "track://" + e.$parent.track.uuid != $scope.status.startupTrack
            }, click: function (e) {
                $http({
                    url: "/api/v1/bot/i/" + currentInstanceId + "/settings",
                    method: "POST",
                    data: {startupTrack: ""},
                    headers: {"Content-Type": "application/json"}
                }).success(function () {
                    $scope.status.startupTrack = "", $alert({
                        title: t("Saved"),
                        content: t("Your settings have been saved."),
                        type: "info",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    }), $state.transitionTo($state.current, $stateParams, {reload: !0, inherit: !1, notify: !0})
                })
            }
        }), $scope.dropdown.push({
            text: t("Play when idle"), hide: function (e) {
                if (e.$parent.state.bulk) {
                    var t = [];
                    if (e.$parent.state.tracks.forEach(function (e) {
                        e.bulkSelected && t.push(e.uuid)
                    }), t.length > 1) return !0
                } else if ("folder" == e.$parent.track.type || e.$parent.track.virtual) return !0;
                return !e.$parent.track.uuid || "track://" + e.$parent.track.uuid == $scope.status.idleTrack
            }, click: function (e) {
                $http({
                    url: "/api/v1/bot/i/" + currentInstanceId + "/settings",
                    method: "POST",
                    data: {idleTrack: "track://" + e.$parent.track.uuid},
                    headers: {"Content-Type": "application/json"}
                }).success(function () {
                    $scope.status.idleTrack = "track://" + e.$parent.track.uuid, $alert({
                        title: t("Saved"),
                        content: t("Your settings have been saved."),
                        type: "info",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    }), $state.transitionTo($state.current, $stateParams, {reload: !0, inherit: !1, notify: !0})
                })
            }
        }), $scope.dropdown.push({
            text: t("Don't play when idle"), hide: function (e) {
                if (e.$parent.state.bulk) {
                    var t = [];
                    if (e.$parent.state.tracks.forEach(function (e) {
                        e.bulkSelected && t.push(e.uuid)
                    }), t.length > 1) return !0
                } else if ("folder" == e.$parent.track.type || e.$parent.track.virtual) return !0;
                return !e.$parent.track.uuid || "track://" + e.$parent.track.uuid != $scope.status.idleTrack
            }, click: function (e) {
                $http({
                    url: "/api/v1/bot/i/" + currentInstanceId + "/settings",
                    method: "POST",
                    data: {idleTrack: ""},
                    headers: {"Content-Type": "application/json"}
                }).success(function () {
                    $scope.status.idleTrack = "", $alert({
                        title: t("Saved"),
                        content: t("Your settings have been saved."),
                        type: "info",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    }), $state.transitionTo($state.current, $stateParams, {reload: !0, inherit: !1, notify: !0})
                })
            }
        })), $scope.playlists && $scope.hasPriv($scope.userInfo, $scope.PRIV_ADDTO_PLAYLIST) && ($scope.dropdown.push({
            divider: !0,
            hide: function (e) {
                if (e.$parent.state.bulk) {
                    var t = [];
                    if (e.$parent.state.tracks.forEach(function (e) {
                        e.bulkSelected && t.push(e.uuid)
                    }), t.length > 1) return !0
                } else if ("folder" == e.$parent.track.type || e.$parent.track.virtual) return !0;
                return !1
            }
        }), $scope.playlists.forEach(function (e) {
            $scope.dropdown.push({
                text: t("Add to") + " " + e.name, hide: function (e) {
                    return !e.$parent.state.bulk && !("folder" != e.$parent.track.type && !e.$parent.track.virtual)
                }, click: function (t) {
                    if (t.$parent.state.bulk) {
                        var n = [];
                        t.$parent.state.tracks.forEach(function (e) {
                            e.bulkSelected && n.push(e.uuid)
                        }), $scope.addToPlaylist(e.uuid, n)
                    } else $scope.addToPlaylist(e.uuid, t.$parent.track.uuid)
                }
            })
        }))
    }, $scope.rebuildDropdown();
    var searchRegex = new RegExp("", "i");
    if ($scope.$watch("search", function (e, t) {
        searchRegex = new RegExp(e, "i")
    }), $scope.filterBySearch = function (e) {
        return "" == $scope.search || !!(searchRegex.test(e.title) || searchRegex.test(e.artist) || searchRegex.test(e.filename))
    }, $scope.clearJobs = function () {
        $http({url: "/api/v1/bot/jobs", method: "DELETE"}).success(function (e) {
            $state.transitionTo($state.current, $stateParams, {reload: !0, inherit: !1, notify: !0})
        })
    }, $scope.clearUploadQueue = function () {
        var e = [];
        $scope.uploadFiles.forEach(function (t) {
            "done" != t.status && "error" != t.status && e.push(t)
        }), $scope.uploadFiles = e
    }, $scope.maxUploadSize = 83886080, $http({url: "/api/v1/bot/uploadInfo", method: "GET"}).success(function (e) {
        $scope.maxUploadSize = e.maxSize
    }), hotkeys.bindTo($scope).add({
        allowIn: ["INPUT"],
        combo: "escape",
        description: t("Clear search"),
        callback: function () {
            $scope.displayConsole = !1, "" != $("#search").val() ? $scope.setSearch("") : $("#search").blur()
        }
    }), $scope.setSearch = function (e) {
        $scope.search = e
    }, $scope.deleteAvatar = function (e) {
        $http({url: "/api/v1/bot/i/" + currentInstanceId + "/avatar", method: "DELETE"}).success(function (e) {
            $alert(e && !1 === e.success ? {
                title: t("Deletion failed"),
                content: t(e.error),
                type: "warning",
                show: !0,
                placement: "top-right",
                duration: 5
            } : {
                title: t("Avatar removed"),
                content: t("The avatar has sucessfully been removed"),
                type: "info",
                show: !0,
                placement: "top-right",
                duration: 5
            })
        })
    }, $scope.setAvatar = function (e) {
        var n = e.shift();
        if (n && !(n.size && n.size > 3145728)) {
            var a = new FileReader;
            a.onload = function (e) {
                $upload.http({
                    url: "/api/v1/bot/i/" + currentInstanceId + "/avatar",
                    headers: {"Content-Type": n.type},
                    data: e.target.result
                }).progress(function (e) {
                    if (e.lengthComputable) {
                        Math.round(e.loaded / e.total * 100)
                    }
                }).success(function (e) {
                    $alert(e && !1 === e.success ? {
                        title: t("Upload failed"),
                        content: t(e.error),
                        type: "warning",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    } : {
                        title: t("Avatar uploaded"),
                        content: t("The avatar has sucessfully been uploaded"),
                        type: "info",
                        show: !0,
                        placement: "top-right",
                        duration: 5
                    })
                }).error(function (e) {
                })
            }, a.readAsArrayBuffer(n)
        }
    }, window.EventSource) {
        var evtSource = new EventSource("/api/v1/events?stoken=" + window.localStorage.token);
        evtSource.onmessage = function (e) {
            console.log(e)
        }, evtSource.addEventListener("log", function (e) {
            $("<li/>").text(e.data).appendTo(".output .lines");
            var t = $(".output");
            t.length && (t[0].scrollTop = t[0].scrollHeight)
        }), evtSource.addEventListener("ilog", function (e) {
            $("<li/>").text("[" + e.lastEventId + "] " + e.data).appendTo(".output .lines");
            var t = $(".output");
            t.length && (t[0].scrollTop = t[0].scrollHeight)
        }), evtSource.addEventListener("info", function (e) {
            $alert({title: "Info", content: t(e.data), type: "info", show: !0, placement: "top-right", duration: 7})
        }), evtSource.addEventListener("track", function (e) {
            var t = JSON.parse(e.data);
            $rootScope.$broadcast("event:newTrack", t)
        }), evtSource.addEventListener("reload-playlists", function (e) {
            $scope.reloadPlaylists()
        });
        var lastFileLoad = null;
        evtSource.addEventListener("reload-files", function (e) {
            null != lastFileLoad && clearTimeout(lastFileLoad), lastFileLoad = setTimeout(function () {
                $scope.reloadFiles(), lastFileLoad = null
            }, 1e3)
        }), evtSource.addEventListener("status", function (e) {
            if (e.lastEventId == currentInstanceId) {
                var t = JSON.parse(e.data);
                cancelPosUpdate && (t.position = $scope.status.position), $scope.status = t, cancelPosUpdate = !1, $scope.connected = !0, $scope.queueVersion != t.queueVersion && ($scope.queueVersion = t.queueVersion, "app.play.queue" == $state.current.name && $state.transitionTo($state.current, $stateParams, {
                    reload: !0,
                    inherit: !1,
                    notify: !0
                })), $scope.$apply()
            }
        }), evtSource.addEventListener("open", function () {
            useRealtime = !0, $scope.connected = !0, $scope.refresh()
        }), evtSource.addEventListener("close", function () {
            useRealtime = !1
        }), evtSource.addEventListener("error", function () {
            useRealtime = !1
        })
    }
    $scope.onFileSelect = function (e, n, a, o) {
        var r = 0;
        e.forEach(function (e) {
            e.nFile = {name: e.name, status: "queued", progress: 0}, $scope.uploadFiles.push(e.nFile), r++
        }), $alert({
            title: t("Upload"),
            content: r + " " + t("files added to upload queue"),
            type: "info",
            show: !0,
            placement: "top-right",
            duration: 3
        });
        var s = function () {
            var o = e.shift();
            if (o) {
                var r = o.nFile;
                if (o.size && o.size > $scope.maxUploadSize) return r.status = "too large", s();
                r.status = "uploading";
                var i = new FileReader;
                i.readAsArrayBuffer(o), i.onload = function (e) {
                    $upload.http({
                        url: "/api/v1/bot/upload?playlist=" + (n || "") + "&folder=" + (a || "") + "&filename=" + encodeURIComponent(o.name),
                        headers: {"Content-Type": o.type},
                        data: e.target.result
                    }).progress(function (e) {
                        if (e.lengthComputable) {
                            var t = Math.round(e.loaded / e.total * 100);
                            r.status = "uploading (" + t + "%)"
                        }
                    }).success(function (e) {
                        e && !1 === e.success ? ($alert({
                            title: t("Upload failed"),
                            content: o.name + "",
                            type: "warning",
                            show: !0,
                            placement: "top-right",
                            duration: 5
                        }), r.status = "failed") : (r.status = "done", r.info = e), s()
                    }).error(function (e) {
                        r.status = "failed", s()
                    })
                }
            }
        };
        s()
    }
}]), angular.module("ts3soundboard-bot").controller("settings.general.wizard", ["$scope", "$http", "settings", "setup", "instance", function (e, t, n, a, o) {
    e.instance = {}, e.setupInstance = function () {
        t({
            url: "/api/v1/bot/i/" + o + "/settings",
            method: "POST",
            data: {authId: e.instance.token},
            headers: {"Content-Type": "application/json"}
        }).success(function (t) {
            t.success ? (a.success = !0, e.$hide()) : e.error = t.error || "Unknown error"
        })
    }
}]).controller("settings.instances.new", ["$scope", "$http", "$state", "$stateParams", function (e, t, n, a) {
    e.instance = {}, e.showBackends = !1, t({url: "/api/v1/bot"}).then(function (t) {
        var n = t.data;
        n && n.allowedBackends && n.allowedBackends.forEach(function (t) {
            switch (t) {
                case"ts3":
                    e.backends.push({val: t, name: "TeamSpeak 3"});
                    break;
                case"discord":
                    e.backends.push({val: t, name: "Discord"})
            }
        }), e.instance.backend = n.allowedBackends[0] || "ts3", 1 != e.backends.length && (e.showBackends = !0)
    }), e.backends = [], e.createInstance = function () {
        t({method: "POST", url: "/api/v1/bot/instances", data: e.instance}).success(function (t) {
            void 0 == t.success || t.success ? (e.$hide(), n.transitionTo(n.current, a, {
                reload: !0,
                inherit: !1,
                notify: !0
            })) : e.error = t.error
        })
    }
}]), angular.module("ts3soundboard-bot").factory("keyboardManager", ["$window", "$timeout", function (e, t) {
    var n = {}, a = {type: "keydown", propagate: !1, inputDisabled: !1, target: e.document, keyCode: !1};
    return n.keyboardEvent = {}, n.bind = function (o, r, s, i) {
        o.$keyBindings || (o.$keyBindings = [], o.$on("$destroy", function () {
            o.$keyBindings.forEach(function (e) {
                n.unbind(e)
            }), delete o.$keyBindings
        })), o.$keyBindings.push(r);
        var l, c, u, d;
        i = angular.extend({}, a, i), r = r.toLowerCase(), c = i.target, "string" == typeof i.target && (c = document.getElementById(i.target)), l = function (n) {
            if (n = n || e.event, i.inputDisabled) {
                var a;
                if (n.target ? a = n.target : n.srcElement && (a = n.srcElement), 3 == a.nodeType && (a = a.parentNode), !("INPUT" != a.tagName && "TEXTAREA" != a.tagName || angular.element(a).hasClass("allowKeybindings") && "space" != r)) return
            }
            n.keyCode ? u = n.keyCode : n.which && (u = n.which);
            var o = String.fromCharCode(u).toLowerCase();
            188 == u && (o = ","), 190 == u && (o = ".");
            for (var l = r.split("+"), c = 0, p = {
                "`": "~",
                1: "!",
                2: "@",
                3: "#",
                4: "$",
                5: "%",
                6: "^",
                7: "&",
                8: "*",
                9: "(",
                0: ")",
                "-": "_",
                "=": "+",
                ";": ":",
                "'": '"',
                ",": "<",
                ".": ">",
                "/": "?",
                "\\": "|"
            }, h = {
                esc: 27,
                escape: 27,
                tab: 9,
                space: 32,
                return: 13,
                enter: 13,
                backspace: 8,
                scrolllock: 145,
                scroll_lock: 145,
                scroll: 145,
                capslock: 20,
                caps_lock: 20,
                caps: 20,
                numlock: 144,
                num_lock: 144,
                num: 144,
                pause: 19,
                break: 19,
                insert: 45,
                home: 36,
                delete: 46,
                end: 35,
                pageup: 33,
                page_up: 33,
                pu: 33,
                pagedown: 34,
                page_down: 34,
                pd: 34,
                left: 37,
                up: 38,
                right: 39,
                down: 40,
                f1: 112,
                f2: 113,
                f3: 114,
                f4: 115,
                f5: 116,
                f6: 117,
                f7: 118,
                f8: 119,
                f9: 120,
                f10: 121,
                f11: 122,
                f12: 123
            }, f = {
                shift: {wanted: !1, pressed: !!n.shiftKey},
                ctrl: {wanted: !1, pressed: !!n.ctrlKey},
                alt: {wanted: !1, pressed: !!n.altKey},
                meta: {wanted: !1, pressed: !!n.metaKey}
            }, g = 0, m = l.length; d = l[g], g < m; g++) {
                switch (d) {
                    case"ctrl":
                    case"control":
                        c++, f.ctrl.wanted = !0;
                        break;
                    case"shift":
                    case"alt":
                    case"meta":
                        c++, f[d].wanted = !0
                }
                d.length > 1 ? h[d] == u && c++ : i.keyCode ? i.keyCode == u && c++ : o == d ? c++ : p[o] && n.shiftKey && (o = p[o]) == d && c++
            }
            if (c == l.length && f.ctrl.pressed == f.ctrl.wanted && f.shift.pressed == f.shift.wanted && f.alt.pressed == f.alt.wanted && f.meta.pressed == f.meta.wanted && (t(function () {
                s(n)
            }, 1), !i.propagate)) return n.cancelBubble = !0, n.returnValue = !1, n.stopPropagation && (n.stopPropagation(), n.preventDefault()), !1
        }, n.keyboardEvent[r] = {
            callback: l,
            target: c,
            event: i.type
        }, c.addEventListener ? c.addEventListener(i.type, l, !1) : c.attachEvent ? c.attachEvent("on" + i.type, l) : c["on" + i.type] = l
    }, n.unbind = function (e) {
        e = e.toLowerCase();
        var t = n.keyboardEvent[e];
        if (delete n.keyboardEvent[e], t) {
            var a = t.event, o = t.target, r = t.callback;
            o.detachEvent ? o.detachEvent("on" + a, r) : o.removeEventListener ? o.removeEventListener(a, r, !1) : o["on" + a] = !1
        }
    }, n
}]), !locales) var locales = {};
locales["de-DE"] = {
    __code: "de-DE",
    __name: "Deutsch",
    Music: "Musik",
    Settings: "Einstellungen",
    "Search...": "Suche...",
    Help: "Hilfe",
    "Connected to": "Verbunden mit",
    "Results for": "Ergebnisse fÃ¼r",
    Edit: "Bearbeiten",
    Delete: "LÃ¶schen",
    Remove: "Entfernen",
    Options: "Optionen",
    Saved: "Gespeichert",
    "Your settings have been saved.": "Die Einstellungen wurden Ã¼bernommen.",
    "Your settings could not be applied.": "Die Einstellungen konnten nicht Ã¼bernommen werden.",
    "Invalid server address, wrong password, security level too high for our identity, server doesn't accept the client version or given default channel non-existant / passworded.": "Falsche Server-Adresse, falsches Kennwort, Sicherheits-Level des Servers zu hoch fÃ¼r die eingestellte IdentitÃ¤t, Server verlangt eine neuere Client-Version oder angegebener Standard-Channel nicht mehr gÃ¼ltig / Kennwort-geschÃ¼tzt.",
    "The bot has been updated, a restart is required.": "Der Bot wurde aktualisiert, ein Neustart ist erforderlich.",
    "Upload files": "Dateien hochladen",
    "Edit files": "Dateien bearbeiten",
    "Delete files": "Dateien lÃ¶schen",
    "Start / stop Bot": "Bot starten / anhalten",
    "Edit users": "Benutzer bearbeiten",
    "Edit bot settings": "Bot-Einstellungen bearbeiten",
    "Manage instances": "Instanzen konfigurieren",
    "Create / delete playlist": "Playlist erstellen / lÃ¶schen",
    "Edit playlist": "Playlist bearbeiten",
    "Create new Playlist": "Neue Playlist erstellen",
    Rename: "Umbenennen",
    "Volume up": "LautstÃ¤rke erhÃ¶hen",
    "Volume down": "LautstÃ¤rke verringern",
    "Seek 5 seconds forward": "5 Sekunden nach vorne spulen",
    "Seek 5 seconds backwards": "5 Sekunden zurÃ¼ck spulen",
    "Say something": "Sage etwas",
    "Say something in another language": "Sage etwas in einer anderen Sprache",
    "Clear search": "Suchbegriff lÃ¶schen",
    "Add URL": "URL hinzufÃ¼gen",
    "Could not add url": "URL konnte nicht hinzugefÃ¼gt werden",
    "Could not add station to list.": "Der Sender konnte nicht hinzugefÃ¼gt werden.",
    "You can only add valid stream URLs in here. YouTube links or links to other services will not work.": "Es kÃ¶nnen nur valide Stream-URLs hinzugefÃ¼gt werden. YouTube-Links oder Links zu anderen Diensten funktionieren hier nicht.",
    "The provided URL could not be recognized as a valid stream. Sorry.": "Die angegebene URL konnte leider nicht als Stream erkannt werden.",
    "Add Folder": "Verzeichnis hinzufÃ¼gen",
    "Rename Playlist": "Playlist umbenennen",
    "Delete Playlist": "Playlist lÃ¶schen",
    "Bulk Mode": "Bulk-Modus",
    "Play folder": "Verzeichnis abspielen",
    "This playlist is still empty.": "Diese Playlist ist noch leer.",
    "You have not uploaded any files yet.": "Es wurden noch keine Dateien hochgeladen.",
    Title: "Titel",
    Artist: "Interpret",
    Album: "Album",
    Genre: "Genre",
    "Failed. Too many jobs in queue.": "Fehlgeschlagen. Zu viele AuftrÃ¤ge in der Warteschleife.",
    "%d files moved": "%d Dateien verschoben",
    "%1$d of %2$d files moved": "%1$d von %2$d Dateien verschoben",
    "%d files copied to clipboard": "%d Dateien in die Zwischenablage kopiert",
    "Toggle bulk mode": "Mehrfach-Verarbeitung ein-/ausschalten",
    "Select all": "Alles auswÃ¤hlen",
    "Select none": "Auswahl aufheben",
    "Copy selection": "Auswahl kopieren",
    "Move copied selection": "Zuvor kopierte Auswahl verschieben",
    "Select track": "Datei auswÃ¤hlen",
    "Move to next entry in list": "zum nÃ¤chsten Eintrag",
    "Select track and move to next entry in list": "aktuellen Eintrag auswÃ¤hlen und zum nÃ¤chsten Eintrag",
    "Select track and move to previous entry in list": "aktuellen Eintrag auswÃ¤hlen und zum vorherigen Eintrag",
    "Move to previous entry in list": "zum vorherigen Eintrag",
    "Play selected entry / enter folder": "Auswahl abspielen oder das Verzeichnis Ã¶ffnen",
    Error: "Fehler",
    "Could not playback the selected file / stream.": "Die gewÃ¤hlte Datei oder der gewÃ¤hlte Stream konnte nicht abgespielt werden.",
    "Do you really want to delete %d tracks?": "Sollen wirklich %d Dateien gelÃ¶scht werden?",
    "Do you really want to delete this track?": "Soll diese Datei wirklich gelÃ¶scht werden?",
    "Username / eMail": "Benutzername / eMail",
    Password: "Kennwort",
    "Bot-Id": "Bot-Id",
    Login: "Anmelden",
    "Transmit-Mode": "Ãœbertragungs-Modus",
    "Default (to channel)": "Standard (in den Channel)",
    "Subscription-Mode (via whisper)": "Abo-Modus (via Whisper)",
    'By default the bot transmits audio to its channel. If you enable the subscription-mode, the bot can send audio to every client that subscribes via the "!sub" command. If no subscription is active, the bot will stream to its channel like in default mode.': 'StandardmÃ¤ÃŸig Ã¼bertrÃ¤gt der Bot Audiosignale in den Channel, in dem er sich befindet. Aktiviert man den Abo-Modus, Ã¼bertragt der Bot an jeden Client, der zuvor Ã¼ber den "!sub"-Befehl ein Abonnement aktiviert hat. Gibt es keine Abonnements, streamt der Bot in den aktuellen Channel, wie im Standard-Modus.',
    "Whisper-Mode is enabled. You can only hear music when you do have an active subscription.": "Abo-Modus aktiviert. Musik kann nur gehÃ¶rt werden, wenn ein aktives Abo besteht.",
    Queue: "Warteschleife",
    "Skip queue": "Warteschleife Ã¼berspringen",
    Enqueue: "der Warteschleife hinzufÃ¼gen",
    "Enqueue next": "vorne in die Warteschleife setzen",
    "Admin queue": "Warteschleife verwalten",
    "The queue is empty.": "Die Warteschleife ist leer.",
    "Remove all": "Alle entfernen",
    "Add to": "HinzufÃ¼gen zu",
    "play on startup": "beim Start abspielen",
    "Play on startup": "Beim Start abspielen",
    "Play when idle": "Abspielen, wenn sonst nichts gespielt wird",
    "play when idle": "abspielen, wenn sonst nichts gespielt wird",
    "Don't play on startup": "Beim Start nicht mehr abspielen",
    "Don't play when idle": "Nicht mehr abspielen, wenn sonst nichts gespielt wird",
    "Hold on... searching...": "Moment bitte... suche...",
    "Nothing found.": "Nichts gefunden.",
    Cancel: "Abbrechen",
    Update: "Aktualisieren",
    "All Music": "Alle Dateien",
    "Add to All Music": "zu Alle Dateien hinzufÃ¼gen",
    Upload: "Hochladen",
    "Radio Stations": "Radio",
    "New playlist": "Neue Playlist",
    "Show advanced options": "Erweiterte Optionen anzeigen",
    "Import via youtube-dl": "Mit Hilfe von youtube-dl importieren",
    "Additionally to the playlist, a subfolder will be created inside your root folder.": "Es wird gleichzeitig ein Unterverzeichnis im Hauptverzeichnis fÃ¼r den Import angelegt.",
    "drop files or click here": "Dateien hierher ziehen oder anklicken zum Hochladen",
    'You can also just drag and drop your files onto the "All Music" list or other playlists directly.': "Dateien kÃ¶nnen auch direkt per Drag'n'Drop auf die \"Alle Dateien\"-Liste sowie die Playlisten gezogen werden.",
    Clear: "LÃ¶schen",
    "You haven't uploaded any files recently.": "Es wurden kÃ¼rzlich keine Dateien hochgeladen.",
    "Uploaded Files": "Hochgeladene Dateien",
    "Download Files": "Herunterladen",
    "Add Job": "Job hinzufÃ¼gen",
    "Upload failed": "Hochladen fehlgeschlagen",
    uploading: "hochladen",
    failed: "fehlgeschlagen",
    done: "fertig",
    "files added to upload queue": "Dateien zur Warteschleife hinzugefÃ¼gt",
    Add: "HinzufÃ¼gen",
    Close: "SchlieÃŸen",
    "Instance Settings": "Instanz-Einstellungen",
    "Personal Settings": "PersÃ¶nliche Einstellungen",
    "Name of the Bot": "Name des Bots",
    "Descriptive name for the instance": "Beschreibender Name fÃ¼r die Instanz",
    "Nickname of the Client": "Nickname des Clients",
    "Has to be unique on the server": "Darf nur einmal auf dem Server vorkommen",
    "Enter a name that will be shown to others - it has to be unique on the server": "Trage hier den Namen ein, der anderen angezeigt werden soll - dieser muss auf dem Server einzigartig sein",
    "Hostname / IP": "Hostname / IP",
    "Enter the hostname or IP-Address of the server": "Hier den Hostnamen oder die IP-Adresse des Servers eingeben, zu dem sich der Bot verbinden soll",
    Port: "Port",
    "Enter the server-password here": "Kennwort, welches zum Verbinden mit dem Server gebraucht wird",
    "Enter the port of the server (usually 9987)": "Port des Servers (gewÃ¶hnlich 9987)",
    "Default Channel": "Standard-Channel",
    "To get a list of all available channels, just let the bot connect first.": "Um eine Liste aller verfÃ¼gbaren Channels anzuzeigen, verbinde einfach den Bot mit deinem Server.",
    "Click here to allow the bot to join your server.": "Klicke hier um dem Bot die Erlaubnis zu erteilen, deinen Server zu betreten.",
    "Select a channel that the bot should try to join upon connection. The bot has to already be connected in order to list all available channels": "Einen Channel auswÃ¤hlen, den der Bot bei Verbindung betreten soll. Der Bot muss bereits mit dem Server verbunden sein, damit eine Liste aller verfÃ¼gbarer Channels angezeigt werden kann",
    none: "keine(r)",
    "channel-id or leave empty for default channel": "Channel-Id oder leer lassen um den Standard-Channel zu nutzen",
    "Ch-Password": "Ch-Kennwort",
    "Enter the channel-password if one is actually set": "Wenn ein Channel-Kennwort fÃ¼r den ausgewÃ¤hlten Channel gesetzt wurde, dieses hier eintragen",
    Identity: "IdentitÃ¤t",
    Volume: "LautstÃ¤rke",
    Locale: "Sprache",
    "Fade duration (in ms, 0 to disable)": "Blenden-Dauer (in ms, 0 zum Deaktivieren)",
    "Each client needs to have a unique identity. Privileges on the server always refer to this identity. You can create your own in your local TS Client and export it. In the exported file, you can then find the identity-string.": "Jeder Client / Bot muss eine eindeutige IdentitÃ¤t haben. SÃ¤mtliche Berechtigungen auf dem Server sind zu dieser IdentitÃ¤t auf den Servern hinterlegt. Um eine eigene IdentitÃ¤t zu erstellen, muss die entsprechende Funktion im Client auf dem eigenen Desktop-Rechner verwendet werden. Dort lassen sich IdentitÃ¤ten exportieren - und in der erstellten IdentitÃ¤ts-Datei findet sich dann die IdentitÃ¤ts-Zeichenfolge.",
    "For security reasons this is always recommended.": "Aus Sicherheits-GrÃ¼nden wird die eigene Erstellung einer IdentitÃ¤t ausdrÃ¼cklich empfohlen.",
    "You can define an external service to be used for Text-to-Speech here. Enter a valid URL here and use the variables __TEXT and __LOCALE - those get replaced by the corresponding text or locale setting later on.": "Hier kann ein externer Text-zu-Sprache-Dienstleister angegeben werden. Die Variablen __TEXT und __LOCALE werden durch den zu sprechenden Text bzw. die eingestellte Spracheinstellung ersetzt.",
    "The default-locale that will be used as __LOCALE parameter, when no locale has been specified explicitely.": "Die Standard-Sprache, die fÃ¼r die Variable __LOCALE verwendet wird, sofern keine andere Einstellung explizit angegegen wurde.",
    "Set track info as description": "Track-Information in die Client-Beschreibung schreiben",
    "Welcome users via TTS": "Besucher via TTS begrÃ¼ÃŸen",
    "Announce tracks": "Songs ankÃ¼ndigen",
    "If checked, the bot will announce the songs it plays back in the channel chat.": "Wenn aktiviert, wird der Bot sÃ¤mtliche Song-Namen in den Channel-Chat schreiben.",
    "Announce-String": "AnkÃ¼ndigungs-Text",
    "The text that should be displayed when announcing a text. Use %a for artist, %t for title or %s for a combination of both (preferred).": "Der Text, der bei einer AnkÃ¼ndigung verwendet werden soll. Folgende Platzhalter werden akzeptiert: %a fÃ¼r Interpret, %t fÃ¼r Titel und %s fÃ¼r eine Kombination aus beidem (wird empfohlen).",
    "%i will get replaced by the username of the user that requested the track.": "%i wird mit dem Namen des Benutzers ersetzt, der die Wiedergabe gestartet hat.",
    "Make sure the bot has the privilege to write there or it won't work.": "Damit dies funktionieren kann, braucht der Bot die Rechte, in den Channel schreiben zu dÃ¼rfen.",
    "Enable ducking": "LautstÃ¤rke-Reduzierung aktivieren",
    "Reduce the volume of the music when someone else in the channel is talking.": "Reduziert die LautstÃ¤rke der Musik, sobald jemand im Channel spricht.",
    "Duck to (in %)": "Reduzieren auf (in %)",
    'Become "Channel-Commander"': '"Channel-Commander" werden',
    "Sets the Channel-Commander-Flag for the bot. This requires that the bot has the permissions to do so.": "Setzt das Channel-Commander-Flag fÃ¼r den Bot. Der Bot muss die Erlaubnis haben, dies zu tun.",
    "Stick to channel": "An den Channel binden",
    "When the bot gets moved, it will try to always go back to the configured channel. If disabled, the bot will remember the last channel it has been.": "Ist diese Funktion aktiviert, versucht der Bot immer wieder in den konfigurierten Channel zurÃ¼ck zu wechseln, sollte jemand ihn verschieben. Ist diese Funktion deaktiviert, wird sich der Bot statt dessen den zuletzt besuchten Raum merken.",
    "Ignore commands via server-chat": "Server-Chat fÃ¼r Kommandos ignorieren",
    "Ignore commands via private message": "Privat-Nachrichten fÃ¼r Kommandos ignorieren",
    "Ignore commands via channel-chat": "Channel-Chat fÃ¼r Kommandos ignorieren",
    Theme: "Erscheinungsbild",
    Language: "Sprache (Language)",
    "Save changes": "Ã„nderungen speichern",
    Save: "Speichern",
    Information: "Informationen",
    Limits: "Limitierungen",
    Commands: "Befehle",
    "Formats &amp; Codecs": "Formate &amp; Codecs",
    Changelog: "Changelog",
    "About...": "Ãœber...",
    "Max. Users": "Max. Benutzer",
    "Max. Files": "Max. Dateien",
    "Max. Space": "Max. Speicherplatz",
    "Max. Playlists": "Max. Playlisten",
    "Max. Instances": "Max. Instanzen",
    "Memory Usage": "Arbeitsspeicher-Verbrauch",
    "Anonymous usage": "Anonyme Nutzung",
    "Registers a new user bound to the TeamSpeak-Account you are using. This account has no privileges by default but can be edited by the bot administrators.": "Registriert einen neuen Benutzernamen, der an den TeamSpeak-Account gebunden wird. Dieser Account hat keine Rechte und muss zuerst vom Administrator freigeschaltet werden.",
    "Display the currently playing song": "den aktuell wiedergegebenen Song anzeigen",
    "Authorized users": "Authentifizierte Benutzer",
    "change your password to <value>": "Kennwort zu <value> Ã¤ndern",
    "The icons represent the required permissions.": "Die Symbole reprÃ¤sentieren die fÃ¼r die Funktion benÃ¶tigten Rechte.",
    "playback the next track": "nÃ¤chsten Song wiedergeben",
    "only when a playlist is active": "nur wenn eine Playlist aktiv ist",
    "search for tracks": "nach einem Song suchen",
    "playback a track by its id or search for a track and playback the first match": "nach einem Titel suchen und den ersten Treffer wiedergeben",
    "resume queue playback": "Warteschleife wieder abspielen",
    "enqueue a track by its id or search for a track and enqueue the first match": "nach einem Titel suchen und den ersten Treffer der Warteschleife hinzufÃ¼gen",
    "prepend a track by its id or search for a track and prepend the first match to the queue": "nach einem Titel suchen und den ersten Treffer vorne in die Warteschleife stellen",
    "playback the previous track": "vorherigen Song wiedergeben",
    "stop playback": "Wiedergabe stoppen",
    "stop playback and remove idle-track": "Wiedergabe stoppen und Idle-Track lÃ¶schen",
    "increase the volume": "LautstÃ¤rke erhÃ¶hen",
    "decrease the volume": "LautstÃ¤rke verringern",
    "set the volume to <value> (between 0 and 100)": "LautstÃ¤rke auf <value> setzen (zwischen 0 und 100)",
    "starts playing back the playlist <playlistname>": "startet die Wiedergabe der Playlist <playlistname>",
    "stream from <url>; this may be http-streams like shoutcast / icecast or just remote soundfiles": "<url> wiedergeben; dies kann ein HTTP-Stream wie Shout- oder Icecast sein oder z.B. MP3-Dateien auf anderen Servern",
    "playback an <url> via external youtube-dl (if enabled); beware: the file will be downloaded first and played back afterwards, so there might be a slight delay before playback starts": "<url> herunterladen und wiedergeben; Achtung: die Datei wird zuerst heruntergeladen und im Anschluss abgespielt. Es kann also ein bisschen Dauern, bis die Wiedergabe startet",
    "enqueue an <url> via external youtube-dl (if enabled); beware: the file will be downloaded first and played back afterwards, so there might be a slight delay before playback starts": "<url> herunterladen und in Warteschleife einreihen; Achtung: die Datei wird zuerst heruntergeladen und im Anschluss abgespielt. Es kann also ein bisschen Dauern, bis die Wiedergabe startet",
    "additionally, the file will be stored": "die Datei wird unter Alle Dateien abgespeichert",
    "toggle shuffle": "Zufalls-Wiedergabe aktivieren/deaktivieren",
    "toggle repeat": "Wiederholung aktivieren/deaktivieren",
    "Supported File-Formats": "UnterstÃ¼tzte Datei-Formate",
    "Supported Codecs": "UnterstÃ¼tzte Codecs",
    searchstring: "Suchbegriff",
    "This list will only contain major changes for now.": "Diese Liste zeigt derzeit nur die grÃ¶ÃŸten Ã„nderungen an.",
    "change transfer mode; 0 = to channel, 1 = subscription mode": "Transfer-Modus Ã¤ndern; 0 = in den Channel, 1 = Abo-Modus (via Whisper)",
    "Subscribe to bot": "Bot abonnieren",
    "Unsubscribe from bot": "Bot-Abo beenden",
    "subscription transfer-mode only": "nur im Abo-Transfer-Modus",
    "add subscription for channel (the user is currently in)": "den Channel in dem sich der Nutzer befindet den Bot abonnieren lassen",
    "remove subscription for channel (the user is currently in)": "Abo des Channels in dem sich der Nutzer befindet abbestellen",
    "use text-to-speech (if configured) to say the given text": "text-to-speech nutzen, um den angegebenen Text wiederzugeben",
    Create: "Erstellen",
    "Your settings have been applied.": "Die Einstellungen wurden Ã¼bernommen.",
    "Add Instance": "Instanz erstellen",
    Instance: "Instanz",
    Instances: "Instanzen",
    Select: "AuswÃ¤hlen",
    "Each instance represents a bot that can connect to your server. All instance share the same users with their privileges and files.": "Jede Instanz reprÃ¤sentiert einen Bot, der sich zu einem Server verbinden kann. Alle Instanzen teilen sich die Benutzeraccounts, Rechte und Dateien.",
    "To create a new instance, click the corresponding button and enter a new nick-name. Afterwards select the newly created bot (selected instances are indicated by a yellowish background) and go to General Settings to enter the credentials for the new instance. Then you can launch the new instance.": "Um eine neue Instanz zu erstellen, klicke zuerst auf den entsprechenden Button und wÃ¤hle einen neuen Nick-Namen. Danach muss die neu erstellte Instanz ausgewÃ¤hlt werden und in den allgemeinen Einstellungen die Server-Einstellungen gesetzt werden. Erst danach kann die neue Instanz gestartet werden.",
    "If you have only one instance, it is selected by default.": "Sollte es nur eine Instanz geben, so ist diese standardmÃ¤ÃŸig ausgewÃ¤hlt",
    "Do you really want to delete this instance?": "Soll die gewÃ¤hlte Instanz wirklich gelÃ¶scht werden?",
    "Instance deleted.": "Instanz wurde gelÃ¶scht.",
    "This instance cannot be deleted.": "Diese Instanz kann nicht gelÃ¶scht werden.",
    "Bot Log": "Bot-Log",
    "Instance Log": "Instanz-Log",
    Info: "Info",
    Username: "Benutzername",
    "Repeat Password": "Kennwort wiederholen",
    "Currently bound identity": "Aktuell gebundene IdentitÃ¤t",
    "Bind to another identity": "An neue IdentitÃ¤t binden",
    "don't change": "Keine Ã„nderung",
    "not online / no identity": "nicht online / keine IdentitÃ¤t",
    Privileges: "Rechte",
    "Servergroup-ID": "Servergruppen-ID",
    Servergroup: "Servergruppe",
    "You can bind this account to an TS-identity, so you can use the bot commands from within TS itself. The bot and the user must be online at the same time for you to be able to select the identity.": "Benutzerkonten kÃ¶nnen an TS-IdentitÃ¤ten gebunden werden, so dass man den Bot auch aus dem TS heraus mit Befehlen steuern kann. Um eine IdentitÃ¤t auswÃ¤hlen zu kÃ¶nnen, mÃ¼ssen sowohl der Bot als auch der Benutzer im TS online sein.",
    "You can also bind this account to a whole servergroup. This is useful for guests or servers with a large user base.": "Man kann ein Benutzerkonto auch an eine Servergruppe binden. Das kann fÃ¼r GÃ¤ste oder Server mit groÃŸer Benutzerbasis hilfreich sein.",
    "Add User Account": "Benutzer hinzufÃ¼gen",
    "User Accounts": "Benutzerkonten",
    "Last Login": "Letzter Login",
    "Changes in here might require a relogin or site refresh when editing yourself.": "Ã„nderungen am eigenen Konto kÃ¶nnen einen Relogin oder eine Seitenaktualisierung benÃ¶tigen.",
    "Edit User Account": "Benutzerkonto bearbeiten",
    Rules: "Regeln",
    "Add Rule": "Regel hinzufÃ¼gen",
    Event: "Ereignis",
    Actions: "Aktionen",
    "Add Action": "Aktion hinzufÃ¼gen",
    "Upload Avatar": "Avatar hochladen",
    "Delete Avatar": "Avatar lÃ¶schen",
    "Deletion failed": "LÃ¶schen fehlgeschlagen",
    "Avatar uploaded": "Avatar hochgeladen",
    "The avatar has sucessfully been uploaded": "Der Avatar wurde erfolgreich hochgeladen",
    "Instance not running.": "Instanz ist nicht online.",
    "Avatar removed": "Avatar entfernt",
    "The avatar has sucessfully been removed": "Der Avatar wurde erfolgreich entfernt",
    "The bot could not acquire servergroups for the clients. Please enable b_virtualserver_servergroup_client_list for the bot if you need this feature.": "Der Bot konnte die Servergruppen der Clients nicht abfragen. Bitte gewÃ¤hre dem Bot das Privileg b_virtualserver_servergroup_client_list wenn du das Feature nutzen mÃ¶chtest.",
    "The bot has been accused of flooding the server. Please consider whitelisting.": "Der Bot wurde beschuldigt, den Server zu flooden. Bitte versuche den Bot zu Whitelisten, damit er seine Arbeit erledigen kann.",
    "The bot has been banned.": "Der Bot wurde gebannt.",
    "Shuffle cannot be deactivated if no playlist is active.": "Shuffle kann nur fÃ¼r Playlisten deaktiviert werden.",
    "Download Addons": "Erweiterungen herunterladen",
    "Warning: Addons (especially scripts) are third party content that comes (like the bot itself) without any warranty and may cause increased CPU, RAM and/or bandwidth usage, show unintended behavior, crash your server or make it vulnerable. If you need any help, click the Info-button and contact the author directly as the SinusBot-Staff won't be able to help you.": "Achtung: Erweiterungen (insbesondere Scripts) sind Inhalte von Drittanbietern die (wie auch der Bot selbst) ohne irgendeine Garantie zur VerfÃ¼gung gestellt wird und erhÃ¶hten Verbrauch von CPU, RAM und/oder Daten zur Folge haben, deinen Server crashen oder ihn angreifbar machen kann. Wenn du Hilfe brauchst, dann klicke bitte auf den Info-Link und wende dich direkt an den Autor des Addons, da das SinusBot-Team dir nicht helfen kÃ¶nnen wird.",
    modified: "modifiziert",
    Addons: "Erweiterungen",
    Scripts: "Scripts",
    Themes: "Erscheinungsbilder",
    Locales: "Sprachdaten",
    "Successfully downloaded script.": "Script wurde erfolgreich heruntergeladen.",
    "Successfully downloaded theme. Refresh the page to be able to select it.": "Thema wurde erfolgreich heruntergeladen. Bitte aktualisiere die Seite, um es nutzen zu kÃ¶nnen.",
    "Successfully downloaded locale. Refresh the page to be able to select it.": "Sprachdaten wurden erfolgreich heruntergeladen. Bitte aktualisiere die Seite, um sie nutzen zu kÃ¶nnen.",
    "This is only a selection of addons that are available for the SinusBot. You might want to check the resources section of the forums to find more.": "Dies ist lediglich eine Auswahl an Erweiterungen, die fÃ¼r den SinusBot zur VerfÃ¼gung stehen. FÃ¼r eine grÃ¶ÃŸere Auswahl, besuche bitte den Ressourcen-Bereich im offiziellen Forum.",
    "To be able to move files (drag'n'drop), please enable \"Bulk Mode\" at the top of this page.": 'Um EintrÃ¤ge verschieben zu kÃ¶nnen, aktiviere bitte den "Bulk-Modus" oben rechts auf dieser Seite.',
    'Click here to generate a new bot account. Afterwards make sure you click "Create Bot User" to also generate the required token.': 'Klicke hier um ein neues Bot-Konto zu erstellen. AnschlieÃŸend klicke bitte auf "Create Bot User" um auch das nÃ¶tige Token zu erstellen.'
}, angular.module("ts3soundboard-bot").filter("trackfilter", function () {
    function e(e, t) {
        var n = t.length, a = e.length;
        if (a > n) return !1;
        if (a === n) return e === t;
        e:for (var o = 0, r = 0; o < a; o++) {
            for (var s = e.charCodeAt(o); r < n;) if (t.charCodeAt(r++) === s) continue e;
            return !1
        }
        return !0
    }

    return function (t, n) {
        if ("" === n) return t;
        n = n.toLowerCase().replace(/-/g, "").replace(/\s+/g, " ");
        for (var a, o = [], r = [], s = t.length, i = 0, l = 0, c = 0; c < s && !(l > 30); c++) a = t[c], a.search.indexOf(n) >= 0 ? (r.push(a), l++) : i > 30 || e(n, a.search) && (o.push(a), i++);
        return Array.prototype.concat(r, o)
    }
}).controller("artistPage", ["$scope", "$http", function (e, t) {
}]).controller("playlist", ["$scope", "$rootScope", "$stateParams", "$http", "$filter", "keyboardManager", "$state", "hotkeys", "files", "playlistFiles", "$alert", "$modal", function (e, n, a, o, r, s, i, l, c, u, d, p) {
    if (e.showParents = 0, e.listMode = 0, e.playlistId = a.playlistId || "", e.playlistId && (e.listMode = 1), "app.play.queue" == i.current.name && (e.listMode = 2), "app.play.search" == i.current.name && (e.showParents = 1, o({
        url: "/api/v1/bot/searchExt",
        params: {q: $("#search").val(), t: (new Date).getTime()},
        method: "GET"
    }).success(function (t) {
        e.extResults = t
    })), e.state = {tracks: []}, e.selected = 0, e.parentFolder = a.folder || "", window.localStorage && window.localStorage["sel-" + e.parentFolder] && (e.selected = parseInt(window.localStorage["sel-" + e.parentFolder], 10) || 0), e.limit = 40, e.displayMore = function () {
        e.limit += 40
    }, e.sortKey = window.localStorage ? window.localStorage.sortKey || "title" : "title", 0 != e.listMode && (e.sortKey = ""), e.name = null, e.playUrl = function (e) {
        o({
            method: "POST",
            url: "/api/v1/bot/i/" + currentInstanceId + "/playUrl?url=" + encodeURIComponent(e.url) + "&title=" + encodeURIComponent(e.title) + "&plugin=" + encodeURIComponent(e.plugin)
        }).success(function (t) {
            t.success || d({
                title: "Error",
                content: "Could not stream " + e.n,
                type: "warning",
                show: !0,
                placement: "top-right",
                duration: 5
            })
        })
    }, e.getSelection = function () {
        var t = [], n = r("trackfilter")(e.state.tracks, $("#search").val());
        return n.forEach(function (e) {
            e.bulkSelected && t.push(e.uuid)
        }), {s: n[e.selected], m: t}
    }, e.toggleBulk = function () {
        e.state.bulk = !e.state.bulk, e.state.bulk && e.state.tracks.forEach(function (e) {
            e.bulkSelected = !1
        })
    }, e.deletePlaylist = function () {
        o({url: "/api/v1/bot/playlists/" + a.playlistId, method: "DELETE"}).success(function (t) {
            e.reloadPlaylists(), i.transitionTo("app.play.files", a, {reload: !0, inherit: !1, notify: !0})
        })
    }, e.updatePlaylist = function () {
        o({
            url: "/api/v1/bot/playlists/" + a.playlistId,
            method: "PATCH",
            headers: {"Content-Type": "application/json"},
            data: {update: !0}
        }).success(function (e) {
            d({
                title: "Info",
                content: t("Update in progress. This may take a while."),
                type: "info",
                show: !0,
                placement: "top-right",
                duration: 1
            })
        })
    }, e.reparent = function (n, a) {
        Array.isArray(n) ? o({
            url: "/api/v1/bot/bulk/files",
            method: "POST",
            data: {op: "move", files: n, parent: a},
            headers: {"Content-Type": "application/json"}
        }).success(function (a) {
            a && a.moved && (d(a.moved != n.length ? {
                title: "Info",
                content: sprintf(t("%1$d of %2$d files moved"), a.moved, n.length),
                type: "info",
                show: !0,
                placement: "top-right",
                duration: 1
            } : {
                title: "Info",
                content: sprintf(t("%d files moved"), n.length),
                type: "info",
                show: !0,
                placement: "top-right",
                duration: 1
            }), window.copyList = [], e.reloadList())
        }) : o({
            url: "/api/v1/bot/files/" + n,
            method: "PATCH",
            data: {parent: a},
            headers: {"Content-Type": "application/json"}
        }).success(function () {
            e.reloadList()
        })
    }, e.reorder = function (t, n, r, s) {
        var i = n();
        if (s && s.type && "folder" == s.type) return void (i.m.length > 0 ? e.reparent(i.m, s.uuid) : e.reparent(i.s.uuid, s.uuid));
        var l = e.state.tracks.indexOf(i.s), s = e.state.tracks[t];
        o({
            url: "/api/v1/bot/playlists/" + a.playlistId + "/move",
            method: "POST",
            data: {index: l, target: t},
            headers: {"Content-Type": "application/json"}
        }).success(function () {
            e.reloadList()
        })
    }, e.$on("event:newTrack", function (t, n) {
        if (c.push(n), 0 != e.listMode) {
            if (!u) return;
            e.prepareList(u)
        } else e.prepareList(c)
    }), e.prepareList = function (n) {
        if (e.listSource = "", 1 == e.listMode) {
            if (!n) return;
            if (e.name = n.name, e.listSource = n.source, e.$parent && e.$parent.status && "playlist://" + n.uuid == e.$parent.status.idleTrack && (e.onIdle = !0), e.$parent && e.$parent.status && "playlist://" + n.uuid == e.$parent.status.startupTrack && (e.onStartup = !0), e.state.tracks = [], !n.entries) return;
            n.entries.forEach(function (t, n) {
                if (t.file) {
                    var a = angular.copy(e.$parent.trackLookup[t.file]);
                    if (!a) return;
                    a.pid = n, e.state.tracks.push(a)
                } else {
                    t.pid = n, e.state.tracks.push(t)
                }
            })
        } else if (2 == e.listMode) {
            e.name = t("Queue"), e.state.tracks = n;
            var a = 0;
            n.forEach(function (e) {
                e.pid = a++
            })
        } else {
            if (!n) return;
            n = n.sort(function (t, n) {
                if ("folder" == t.type && "folder" != n.type) return -1;
                if ("folder" != t.type && "folder" == n.type) return 1;
                if (!t[e.sortKey] && !n[e.sortKey]) return 0;
                if (!t[e.sortKey]) return 1;
                if (!n[e.sortKey]) return -1;
                var a = t[e.sortKey].localeCompare(n[e.sortKey]);
                return 0 === a && "album" == e.sortKey && t.track && n.track ? t.track - n.track : a
            }), e.state.tracks = [];
            var o = {uuid: "--", title: "..", type: "folder", virtual: !0};
            e.parentFolder && e.state.tracks.push(o);
            var r;
            if (e.showParents) {
                for (var s = {}, l = 0, c = n.length; l < c; l++) r = n[l], "folder" == r.type && (s[r.uuid] = r);
                e.parentLookup = s
            }
            for (var l = 0, c = n.length; l < c; l++) r = n[l], r.uuid == e.parentFolder && (e.name = r.title, o.uuid = r.parent), r.parent != e.parentFolder && "app.play.search" != i.current.name || e.state.tracks.push(r)
        }
        e.state.tracks.forEach(function (n) {
            n.displayTitle = n.title || n.filename, n.search = ((n.artist || "") + " " + (n.displayTitle || "") + " " + (n.album || "")).toLowerCase(), e.$parent && e.$parent.status && "track://" + n.uuid == e.$parent.status.idleTrack && (n.displayTitle += " (" + t("play when idle") + ")"), e.$parent && e.$parent.status && "track://" + n.uuid == e.$parent.status.startupTrack && (n.displayTitle += " (" + t("play on startup") + ")");
            var a = moment.duration(n.duration / 1e3, "seconds"), o = a.seconds(), r = a.minutes();
            a.hours() > 0 ? n.durationP = a.hours() + ":" + (r < 10 ? "0" + r : r) + ":" + (o < 10 ? "0" + o : o) : n.durationP = r + ":" + (o < 10 ? "0" + o : o)
        })
    }, e.reloadList = function () {
        1 == e.listMode ? o({url: "/api/v1/bot/playlists/" + a.playlistId}).success(function (t) {
            e.prepareList(t)
        }) : 2 == e.listMode ? o({url: "/api/v1/bot/i/" + currentInstanceId + "/queue"}).success(function (t) {
            e.prepareList(t)
        }) : o({url: "/api/v1/bot/files"}).success(function (t) {
            e.prepareList(t)
        })
    }, e.sortBy = function (t) {
        0 == e.listMode && (e.sortKey = t, window.localStorage.sortKey = t, e.reloadList())
    }, e.playFolder = function () {
        if (e.state.tracks && e.state.tracks.length > 0) {
            var a = [];
            if (e.state.tracks.forEach(function (e) {
                e.type && "folder" == e.type || a.push(e.uuid)
            }), !a.length) return;
            var r = Math.floor(Math.random() * a.length);
            o({
                url: "/api/v1/bot/i/" + currentInstanceId + "/play/byId/" + a[r] + "?dir=1",
                method: "POST"
            }).success(function (e) {
                e.success ? n.$broadcast("event:trackChange", null) : d({
                    title: t("Error"),
                    content: t("Could not playback the selected file / stream."),
                    type: "warning",
                    show: !0,
                    placement: "top-right",
                    duration: 5
                })
            })
        }
    }, e.play = function (r, s) {
        if (1 == e.listMode) o({
            url: "/api/v1/bot/i/" + currentInstanceId + "/play/byList/" + a.playlistId + "/" + r,
            method: "POST"
        }).success(function (e) {
            e.success ? n.$broadcast("event:trackChange", null) : d({
                title: t("Error"),
                content: t("Could not playback the selected file / stream."),
                type: "warning",
                show: !0,
                placement: "top-right",
                duration: 5
            })
        }); else if (2 == e.listMode) o({
            url: "/api/v1/bot/i/" + currentInstanceId + "/play/byId/" + s.uuid,
            method: "POST"
        }).success(function (a) {
            a.success ? (n.$broadcast("event:trackChange", null), e.reloadList()) : d({
                title: t("Error"),
                content: t("Could not playback the selected file / stream."),
                type: "warning",
                show: !0,
                placement: "top-right",
                duration: 5
            })
        }); else {
            if (s && "folder" == s.type) return window.localStorage && (window.localStorage["sel-" + e.parentFolder] = e.selected), void ("" == s.uuid ? i.transitionTo("app.play.files", a, {
                reload: !0,
                inherit: !1,
                notify: !0
            }) : i.transitionTo("app.play.files-folder", {folder: s.uuid}, {inherit: !1, reload: !0, notify: !0}));
            o({url: "/api/v1/bot/i/" + currentInstanceId + "/play/byId/" + r, method: "POST"}).success(function (e) {
                e.success ? n.$broadcast("event:trackChange", null) : d({
                    title: t("Error"),
                    content: t("Could not playback the selected file / stream."),
                    type: "warning",
                    show: !0,
                    placement: "top-right",
                    duration: 5
                })
            })
        }
    }, e.select = function (t, n, a) {
        var o = e.selected;
        if (e.selectSource = "mouse", e.selected = t, a.shiftKey) {
            e.state.bulk = !0;
            var s, i, l = r("trackfilter")(e.state.tracks, $("#search").val());
            o > t ? (s = t, i = o) : (s = o, i = t);
            for (var c = s; c <= i; c++) l[c].bulkSelected = !0
        }
    }, e.clearQueue = function () {
        o({
            url: "/api/v1/bot/i/" + currentInstanceId + "/queue",
            method: "PATCH",
            data: [],
            headers: {"Content-Type": "application/json"}
        }).success(function () {
            e.reloadList(), e.reloadPlaylists()
        })
    }, e.remove = function (n) {
        if (1 == e.listMode) if (e.state.bulk) {
            var s = r("trackfilter")(e.state.tracks, $("#search").val()), i = [];
            s.forEach(function (e) {
                e.bulkSelected && i.push(parseInt(e.pid, 10))
            }), confirm(sprintf(t("Do you really want to delete %d tracks?"), i.length)) && o({
                url: "/api/v1/bot/bulk/playlist/" + a.playlistId + "/files",
                method: "POST",
                data: {op: "delete", files: i},
                headers: {"Content-Type": "application/json"}
            }).success(function () {
                e.reloadList(), e.reloadPlaylists()
            })
        } else {
            var s = r("trackfilter")(e.state.tracks, $("#search").val());
            o({url: "/api/v1/bot/playlists/" + a.playlistId + "/" + s[n].pid, method: "DELETE"}).success(function () {
                e.reloadList(), e.reloadPlaylists()
            })
        } else if (2 == e.listMode) if (e.state.bulk) ; else {
            var s = r("trackfilter")(e.state.tracks, $("#search").val());
            o({
                url: "/api/v1/bot/i/" + currentInstanceId + "/queue/" + s[n].pid,
                method: "DELETE"
            }).success(function () {
                e.reloadList()
            })
        } else if (e.state.bulk) {
            var s = r("trackfilter")(e.state.tracks, $("#search").val()), i = [];
            s.forEach(function (e) {
                e.bulkSelected && i.push(e.uuid)
            }), confirm("Do you really want to delete " + i.length + " files?") && o({
                url: "/api/v1/bot/bulk/files",
                method: "POST",
                data: {op: "delete", files: i},
                headers: {"Content-Type": "application/json"}
            }).success(function () {
                e.reloadList()
            })
        } else {
            var s = r("trackfilter")(e.state.tracks, $("#search").val());
            confirm(t("Do you really want to delete this track?")) && o({
                url: "/api/v1/bot/files/" + s[n].uuid,
                method: "DELETE"
            }).success(function () {
                e.reloadList()
            })
        }
    }, 0 != e.listMode) {
        if (!u) return;
        e.prepareList(u)
    } else e.prepareList(c);
    var h;
    e.bulkSelect = function (t, n, a) {
        if (n.virtual || (n.bulkSelected = !n.bulkSelected), !a.shiftKey) return void (h = t);
        var o = r("trackfilter")(e.state.tracks, $("#search").val());
        if (t > h) for (var s = h; s < t; s++) o[s].virtual || (o[s].bulkSelected = n.bulkSelected); else for (var s = t; s < h; s++) o[s].virtual || (o[s].bulkSelected = n.bulkSelected)
    }, e.bulkToggle = function () {
        e.state.all ? (e.state.all = !1, e.bulkNone()) : (e.state.all = !0, e.bulkAll())
    }, e.bulkAll = function (t) {
        t && t.preventDefault(), e.state.bulk = !0, r("trackfilter")(e.state.tracks, $("#search").val()).forEach(function (e) {
            e.virtual || (e.bulkSelected = !0)
        })
    }, e.bulkNone = function (t) {
        t && t.preventDefault(), r("trackfilter")(e.state.tracks, $("#search").val()).forEach(function (e) {
            e.virtual || (e.bulkSelected = !1)
        })
    };
    var f = function () {
        var n = r("trackfilter")(e.state.tracks, $("#search").val()), a = e.getSelection();
        if (!a.length && e.selected >= 0 && e.selected < n.length) {
            n[e.selected].uuid && a.push(n[e.selected].uuid)
        }
        window.copyList = a, d({
            title: t("Info"),
            content: sprintf(t("%d files copied to clipboard"), a.length),
            type: "info",
            show: !0,
            placement: "top-right",
            duration: 1
        })
    }, g = function () {
        window.copyList && window.copyList.length > 0 && e.reparent(window.copyList, e.parentFolder)
    };
    l.bindTo(e).add({
        allowIn: [], combo: "meta+b", description: t("Toggle bulk mode"), callback: function () {
            e.state.bulk = !e.state.bulk
        }
    }), l.bindTo(e).add({
        allowIn: [],
        combo: "meta+a",
        description: t("Select all"),
        callback: e.bulkAll
    }), l.bindTo(e).add({
        allowIn: [],
        combo: "meta+d",
        description: t("Select none"),
        callback: e.bulkNone
    }), l.bindTo(e).add({
        allowIn: [],
        combo: "meta+c",
        description: t("Copy selection"),
        callback: f
    }), l.bindTo(e).add({
        allowIn: [],
        combo: "meta+v",
        description: t("Move copied selection"),
        callback: g
    }), l.bindTo(e).add({
        allowIn: ["INPUT"], combo: "meta+up", callback: function (t) {
            e.selected = 0
        }
    }), l.bindTo(e).add({
        allowIn: ["INPUT"], combo: "meta+down", callback: function (t) {
            e.selected = r("trackfilter")(e.state.tracks, $("#search").val()).length - 1
        }
    }), l.bindTo(e).add({
        allowIn: [], combo: "n f", callback: function (t) {
            e.addFolder()
        }
    }), l.bindTo(e).add({
        allowIn: [], combo: "space", description: t("Select track"), callback: function (t) {
            e.state.bulk = !0, t.preventDefault();
            var n = r("trackfilter")(e.state.tracks, $("#search").val());
            n[e.selected].virtual || (n[e.selected].bulkSelected = !n[e.selected].bulkSelected)
        }
    }), l.bindTo(e).add({
        allowIn: ["INPUT"],
        combo: "down",
        description: t("Move to next entry in list"),
        callback: function (t) {
            $("#search").blur(), t.preventDefault(), e.selectSource = "keyboard";
            var n = r("trackfilter")(e.state.tracks, $("#search").val());
            e.selected = (e.selected + 1) % n.length
        }
    }), l.bindTo(e).add({
        allowIn: ["INPUT"], combo: "pagedown", callback: function (t) {
            $("#search").blur(), t.preventDefault(), e.selectSource = "keyboard";
            var n = r("trackfilter")(e.state.tracks, $("#search").val());
            e.selected = (e.selected + 15) % n.length
        }
    }), l.bindTo(e).add({
        allowIn: ["INPUT"], combo: "pageup", callback: function (t) {
            $("#search").blur(), t.preventDefault(), e.selectSource = "keyboard";
            r("trackfilter")(e.state.tracks, $("#search").val());
            e.selected -= 15, e.selected < 0 && (e.selected = 0)
        }
    }), l.bindTo(e).add({
        allowIn: ["INPUT"],
        combo: "up",
        description: t("Move to previous entry in list"),
        callback: function (t) {
            $("#search").blur(), t.preventDefault(), e.selectSource = "keyboard";
            var n = r("trackfilter")(e.state.tracks, $("#search").val());
            e.selected = (n.length + e.selected - 1) % n.length
        }
    }), l.bindTo(e).add({
        allowIn: ["INPUT"],
        combo: "shift+down",
        description: t("Select track and move to next entry in list"),
        callback: function (t) {
            e.state.bulk = !0, t.preventDefault(), e.selectSource = "keyboard";
            var n = r("trackfilter")(e.state.tracks, $("#search").val());
            n[e.selected].virtual || (n[e.selected].bulkSelected = !n[e.selected].bulkSelected), e.selected = (e.selected + 1) % n.length
        }
    }), l.bindTo(e).add({
        allowIn: ["INPUT"],
        combo: "shift+up",
        description: t("Select track and move to previous entry in list"),
        callback: function (t) {
            e.state.bulk = !0, t.preventDefault(), e.selectSource = "keyboard";
            var n = r("trackfilter")(e.state.tracks, $("#search").val());
            n[e.selected].virtual || (n[e.selected].bulkSelected = !n[e.selected].bulkSelected), e.selected = (n.length + e.selected - 1) % n.length
        }
    }), l.bindTo(e).add({
        combo: "enter", description: t("Play selected entry / enter folder"), callback: function (t) {
            if (-1 != e.selected) {
                t.preventDefault();
                var n = r("trackfilter")(e.state.tracks, $("#search").val()), a = n[e.selected];
                e.play(void 0 != a.pid ? a.pid : a.uuid, a)
            }
        }
    }), l.bindTo(e).add({
        combo: "q", callback: function (t) {
            if (-1 != e.selected) {
                t.preventDefault();
                var n = r("trackfilter")(e.state.tracks, $("#search").val()), a = n[e.selected];
                e.addToQueue(a.uuid)
            }
        }
    }), e.addUrlModal = null, e.addUrl = function (n, r) {
        o({
            url: "/api/v1/bot/url",
            method: "POST",
            data: {url: n, title: r, parent: e.parentFolder},
            headers: {"Content-Type": "application/json"}
        }).success(function (n) {
            e.addUrlModal.hide(), n && n.success ? (e.reloadList(), i.transitionTo(i.current, a, {
                reload: !0,
                inherit: !1,
                notify: !0
            })) : d({
                title: t("Error"),
                content: t("The provided URL could not be recognized as a valid stream. Sorry."),
                type: "warning",
                show: !0,
                placement: "top-right",
                duration: 5
            })
        })
    }, e.addUrlWindow = function () {
        e.addUrlModal = p({
            scope: e,
            template: "/partials/_modal.tpl.html",
            contentTemplate: "/partials/play.addUrl.tpl.html",
            show: !0,
            title: t("Add URL")
        })
    }, e.addFolderModal = null, e.addFolder = function (t) {
        o({
            url: "/api/v1/bot/folders",
            method: "POST",
            headers: {"Content-Type": "application/json"},
            data: {name: t, parent: e.parentFolder}
        }).success(function (t) {
            e.addFolderModal.hide(), i.transitionTo(i.current, a, {reload: !0, inherit: !1, notify: !0})
        })
    }, e.addFolderWindow = function () {
        e.addFolderModal = p({
            scope: e,
            template: "/partials/_modal.tpl.html",
            contentTemplate: "/partials/play.addFolder.tpl.html",
            show: !0,
            title: t("Add Folder")
        })
    }, e.renamePlaylistModal = null, e.renamePlaylist = function (t) {
        t != e.name && o({
            url: "/api/v1/bot/playlists/" + a.playlistId,
            method: "PATCH",
            headers: {"Content-Type": "application/json"},
            data: {name: t}
        }).success(function (t) {
            e.renamePlaylistModal.hide(), e.reloadPlaylists(), i.transitionTo(i.current, a, {
                reload: !0,
                inherit: !1,
                notify: !0
            })
        })
    }, e.renamePlaylistWindow = function () {
        e.playlistRenameName = e.name, e.renamePlaylistModal = p({
            scope: e,
            template: "/partials/_modal.tpl.html",
            contentTemplate: "/partials/play.renamePlaylist.tpl.html",
            show: !0,
            title: t("Rename Playlist")
        })
    }, e.playlistDropdown = [{
        text: t("Rename Playlist"),
        click: e.renamePlaylistWindow,
        divider: !1
    }, {
        text: t("Delete Playlist"),
        click: e.deletePlaylist,
        divider: !1
    }, {divider: !0}, e.$parent && e.$parent.status && "playlist://" + a.playlistId == e.$parent.status.startupTrack ? {
        text: t("Don't play on startup"),
        click: function (n) {
            o({
                url: "/api/v1/bot/i/" + currentInstanceId + "/settings",
                method: "POST",
                data: {startupTrack: ""},
                headers: {"Content-Type": "application/json"}
            }).success(function () {
                e.$parent.status.startupTrack = "", d({
                    title: t("Saved"),
                    content: t("Your settings have been saved."),
                    type: "info",
                    show: !0,
                    placement: "top-right",
                    duration: 5
                }), i.transitionTo(i.current, a, {reload: !0, inherit: !1, notify: !0})
            })
        }
    } : {
        text: t("Play on startup"), click: function (n) {
            o({
                url: "/api/v1/bot/i/" + currentInstanceId + "/settings",
                method: "POST",
                data: {startupTrack: "playlist://" + a.playlistId},
                headers: {"Content-Type": "application/json"}
            }).success(function () {
                e.$parent.status.startupTrack = "playlist://" + a.playlistId, d({
                    title: t("Saved"),
                    content: t("Your settings have been saved."),
                    type: "info",
                    show: !0,
                    placement: "top-right",
                    duration: 5
                }), i.transitionTo(i.current, a, {reload: !0, inherit: !1, notify: !0})
            })
        }
    }, e.$parent && e.$parent.status && "playlist://" + a.playlistId == e.$parent.status.idleTrack ? {
        text: t("Don't play when idle"),
        click: function (n) {
            o({
                url: "/api/v1/bot/i/" + currentInstanceId + "/settings",
                method: "POST",
                data: {idleTrack: ""},
                headers: {"Content-Type": "application/json"}
            }).success(function () {
                e.$parent.status.idleTrack = "", d({
                    title: t("Saved"),
                    content: t("Your settings have been saved."),
                    type: "info",
                    show: !0,
                    placement: "top-right",
                    duration: 5
                }), i.transitionTo(i.current, a, {reload: !0, inherit: !1, notify: !0})
            })
        }
    } : {
        text: t("Play when idle"), click: function (n) {
            o({
                url: "/api/v1/bot/i/" + currentInstanceId + "/settings",
                method: "POST",
                data: {idleTrack: "playlist://" + a.playlistId},
                headers: {"Content-Type": "application/json"}
            }).success(function () {
                e.$parent.status.idleTrack = "playlist://" + a.playlistId, d({
                    title: t("Saved"),
                    content: t("Your settings have been saved."),
                    type: "info",
                    show: !0,
                    placement: "top-right",
                    duration: 5
                }), i.transitionTo(i.current, a, {reload: !0, inherit: !1, notify: !0})
            })
        }
    }], e.listSource && e.playlistDropdown.splice(0, 0, {
        text: t("Update Playlist"),
        click: e.updatePlaylist,
        divider: !1
    })
}]), angular.module("ts3soundboard-bot").factory("progressInterceptor", ["$injector", "$q", function (e, t) {
    var n = null, a = 0, o = function () {
        return n = n || e.get("ngProgressLite")
    }, r = function () {
        var e = o();
        a--, 0 === a ? e.done() : e.inc()
    }, s = function () {
        var e = o();
        0 === a && e.start(), a++
    };
    return {
        request: function (e) {
            return e.ignoreLoader || s(), e
        }, requestError: function (e) {
            return e.ignoreLoader || r(), e
        }, response: function (e) {
            return e.config.ignoreLoader || r(), e
        }, responseError: function (e) {
            return e.config.ignoreLoader || r(), t.reject(e)
        }
    }
}]), angular.module("ts3soundboard-bot").controller("settings.rules.addaction", ["$scope", "$stateParams", "$http", "$filter", "keyboardManager", "$modal", "$alert", "$state", function (e, t, n, a, o, r, s, i) {
    e.editAction = {paramValues: {}}, e.actions = [], e.actionById = {}, n({url: "/api/v1/bot/rules/actions"}).success(function (t) {
        e.actions = t, t.forEach(function (t) {
            e.actionById[t.id] = t
        })
    }), e.addAction = function (a) {
        var o = {};
        e.actionById[a.action].params && e.actionById[a.action].params.forEach(function (e) {
            o[e.field] = a.paramValues[e.field], "*uint32" != e.type && "uint32" != e.type && "*int32" != e.type && "int32" != e.type || (o[e.field] = parseInt(o[e.field], 10))
        }), e.curRule.actions = e.curRule.actions || [], e.curRule.actions.push({
            type: a.action,
            params: o
        }), n({
            url: "/api/v1/bot/i/" + currentInstanceId + "/rules/" + e.curRule.uuid,
            method: "PUT",
            data: e.curRule.actions
        }).success(function (n) {
            e.$hide(), i.transitionTo(i.current, t, {reload: !0, inherit: !1, notify: !0})
        })
    }
}]).controller("settings.rules.new", ["$scope", "$stateParams", "$http", "$filter", "keyboardManager", "$modal", "$alert", "$state", function (e, t, n, a, o, r, s, i) {
    e.current = {
        paramFilters: {},
        paramValues: {}
    }, e.events = [], e.eventById = {}, n({url: "/api/v1/bot/rules/events"}).success(function (t) {
        e.events = t, t.forEach(function (t) {
            e.eventById[t.id] = t
        })
    }), e.createRule = function (a) {
        console.log(a);
        var o = {};
        e.eventById[a.event].params.forEach(function (e) {
            a.paramFilters[e.field] && (o[e.field] = a.paramValues[e.field], "*uint32" != e.type && "uint32" != e.type && "*int32" != e.type && "int32" != e.type || (o[e.field] = parseInt(o[e.field], 10)))
        }), n({
            method: "POST",
            url: "/api/v1/bot/i/" + currentInstanceId + "/rules",
            data: {name: a.name, type: a.event, params: o}
        }).success(function (n) {
            void 0 == n.success || n.success ? (e.$hide(), i.transitionTo(i.current, t, {
                reload: !0,
                inherit: !1,
                notify: !0
            })) : e.error = n.error
        })
    }
}]), angular.module("ts3soundboard-bot").controller("settings.users.new", ["$scope", "$http", "$state", "$stateParams", function (e, t, n, a) {
    e.error = null, e.PRIV_LOGIN = 1, e.PRIV_LIST_FILE = 2, e.PRIV_UPLOAD_FILE = 4, e.PRIV_DELETE_FILE = 8, e.PRIV_EDIT_FILE = 16, e.PRIV_CREATE_PLAYLIST = 32, e.PRIV_DELETE_PLAYLIST = 64, e.PRIV_ADDTO_PLAYLIST = 128, e.PRIV_STARTSTOP = 256, e.PRIV_EDITUSERS = 512, e.PRIV_CHANGENICK = 1024, e.PRIV_BROADCAST = 2048, e.PRIV_PLAYBACK = 4096, e.PRIV_EDITBOT = 65536, e.user = {privileges: 0}, e.clientlist = [], t({
        method: "GET",
        url: "/api/v1/bot/i/" + currentInstanceId + "/channels"
    }).success(function (t) {
        if (t) {
            var n = [];
            t.forEach(function (e) {
                e.clients.forEach(function (t) {
                    t.room = e.name, n.push(t)
                })
            }), e.clientlist = n
        }
    }), e.hasPriv = function (e, t) {
        return 0 != (e.privileges & t)
    }, e.togglePriv = function (e, t) {
        0 != (e.privileges & t) ? e.privileges &= t : e.privileges |= t
    }, e.createUser = function () {
        t({method: "POST", url: "/api/v1/bot/users", data: e.user}).success(function (t) {
            void 0 == t.success || t.success ? (e.$hide(), n.transitionTo(n.current, a, {
                reload: !0,
                inherit: !1,
                notify: !0
            })) : e.error = t.error
        })
    }
}]), angular.module("ts3soundboard-bot").controller("users", ["$scope", "$stateParams", "$http", "$filter", "keyboardManager", "$modal", "$alert", "users", "instances", function (e, n, a, o, r, s, i, l, c) {
    e.reload = function () {
        a({url: "/api/v1/bot/users"}).success(function (t) {
            e.users = t, t.forEach(function (e) {
                e.lastLogin && (e.lastLogin = new Date(e.lastLogin))
            })
        }), a({url: "/api/v1/bot/instances"}).success(function (t) {
            e.instances = t
        })
    }, e.users = l, e.instances = c, e.PRIVS = [{
        name: "PRIV_ENQUEUE",
        value: 8192,
        icon: "glyphicon-plus",
        title: "Enqueue",
        enabled: !0,
        instance: !0
    }, {
        name: "PRIV_ENQUEUENEXT",
        value: 16384,
        icon: "glyphicon-play-circle",
        title: "Skip queue",
        enabled: !0,
        instance: !0
    }, {
        name: "PRIV_ADMINQUEUE",
        value: 32768,
        icon: "glyphicon-remove-circle",
        title: "Admin queue",
        enabled: !0,
        instance: !0
    }, {
        name: "PRIV_PLAYBACK",
        value: 4096,
        icon: "glyphicon-music",
        title: "Playback",
        enabled: !0,
        instance: !0
    }, {
        name: "PRIV_STARTSTOP",
        value: 256,
        icon: "glyphicon-off",
        title: "Start / stop Bot",
        enabled: !0,
        instance: !0
    }, {
        name: "PRIV_EDITBOT",
        value: 65536,
        icon: "glyphicon-cog",
        title: "Edit bot settings",
        enabled: !0,
        instance: !0
    }, {name: "PRIV_LOGIN", value: 1, icon: "glyphicon-eye-open", title: "Login", enabled: !0}, {
        name: "PRIV_LIST_FILE",
        value: 2,
        icon: "glyphicon-eye-open",
        title: "List files",
        enabled: !1
    }, {
        name: "PRIV_UPLOAD_FILE",
        value: 4,
        icon: "glyphicon-cloud-upload",
        title: "Upload files",
        enabled: !0
    }, {
        name: "PRIV_DELETE_FILE",
        value: 8,
        icon: "glyphicon-remove-sign",
        title: "Delete files",
        enabled: !0
    }, {
        name: "PRIV_EDIT_FILE",
        value: 16,
        icon: "glyphicon-edit",
        title: "Edit files",
        enabled: !0
    }, {
        name: "PRIV_CREATE_PLAYLIST",
        value: 32,
        icon: "glyphicon-duplicate",
        title: "Create / delete playlist",
        enabled: !0
    }, {
        name: "PRIV_ADDTO_PLAYLIST",
        value: 128,
        icon: "glyphicon-copy",
        title: "Edit playlist",
        enabled: !0
    }, {
        name: "PRIV_EDITINSTANCE",
        value: 131072,
        icon: "glyphicon-th-list",
        title: "Manage instances",
        enabled: !0
    }, {
        name: "PRIV_EDITUSERS",
        value: 512,
        icon: "glyphicon-user",
        title: "Edit users",
        enabled: !0
    }, {
        name: "PRIV_CHANGENICK",
        value: 1024,
        icon: "glyphicon-eye-open",
        title: "Change nickname",
        enabled: !1,
        instance: !0
    }, {
        name: "PRIV_BROADCAST",
        value: 2048,
        icon: "glyphicon-eye-open",
        title: "Broadcast messages",
        enabled: !1
    }], e.hasPriv = function (e, t) {
        return 0 != (e.privileges & t)
    }, e.hasInstancePriv = function (e, t, n) {
        return 0 != (t.privileges[e.id] & n)
    }, e.togglePriv = function (e, n) {
        a({
            method: "PATCH",
            url: "/api/v1/bot/users/" + e.id,
            data: {privileges: e.privileges ^ n},
            headers: {"Content-Type": "application/json"}
        }).success(function (t) {
            e.privileges = t.privileges
        }).error(function (e) {
            i({title: "Error", content: t(e.error), type: "warning", show: !0, placement: "top-right", duration: 5})
        })
    }, e.toggleInstancePriv = function (e, n, o) {
        a({
            method: "PATCH",
            url: "/api/v1/bot/i/" + n.uuid + "/users/" + e.id,
            data: {privileges: (n.privileges[e.id] || 0) ^ o},
            headers: {"Content-Type": "application/json"}
        }).success(function (t) {
            n.privileges[e.id] = (n.privileges[e.id] || 0) ^ o
        }).error(function (e) {
            i({title: "Error", content: t(e.error), type: "warning", show: !0, placement: "top-right", duration: 5})
        })
    }, e.addUser = function () {
        s({
            template: "/partials/_modal.tpl.html",
            contentTemplate: "/partials/settings.users.new.tpl.html",
            show: !0,
            title: t("Add User Account"),
            placement: "center"
        })
    }, e.editUser = function (e) {
        s.open({
            template: "/partials/_modal.tpl.html",
            contentTemplate: "/partials/settings.users.edit.tpl.html",
            show: !0,
            title: t("Edit User Account"),
            placement: "center",
            controller: "users.edit",
            resolve: {
                user: function () {
                    return e
                }
            }
        })
    }, e.deleteUser = function (n) {
        a({method: "DELETE", url: "/api/v1/bot/users/" + n.id}).success(function () {
            e.reload()
        }).error(function (e) {
            i({title: "Error", content: t(e.error), type: "warning", show: !0, placement: "top-right", duration: 5})
        })
    }
}]).controller("users.edit", ["$scope", "$stateParams", "$http", "$filter", "keyboardManager", "$modal", "$alert", "$state", "user", function (e, n, a, o, r, s, i, l, c) {
    e.user = angular.copy(c), e.user.tsuidnew = e.user.tsuid, e.clientlist = [], a({
        method: "GET",
        url: "/api/v1/bot/i/" + currentInstanceId + "/channels"
    }).success(function (t) {
        if (t) {
            var n = [];
            n.push({uid: "##del", nick: "-- Delete"}), t.forEach(function (e) {
                e.clients && e.clients.forEach(function (t) {
                    t.room = e.name, n.push(t)
                })
            }), e.clientlist = n
        }
    }), e.save = function (o) {
        var r = {};
        e.user.password && e.user.password == e.user.password2 && (r.password = e.user.password), e.user.tsuidnew && e.user.tsuidnew != e.user.tsuid && (r.tsuid = e.user.tsuidnew), r.tsgid = e.user.tsgid, a({
            url: "/api/v1/bot/users/" + c.id,
            method: "PATCH",
            headers: {"Content-Type": "application/json"},
            data: r
        }).success(function () {
            e.$hide(), l.transitionTo(l.current, n, {reload: !0, inherit: !1, notify: !0}), i({
                title: t("Saved"),
                content: t("Your settings have been saved."),
                type: "info",
                show: !0,
                placement: "top-right",
                duration: 5
            })
        }).error(function (e) {
            i({title: "Error", content: t(e.error), type: "warning", show: !0, placement: "top-right", duration: 5})
        })
    }
}]);