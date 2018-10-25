define(["jquery"], function(t) {

    var e = {
        ssc: {
            num: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
            num_len: 10,
            col_td: 8,
            open_len: 5,
            zj_val: 4,
            max: 9,
            da: [5, 6, 7, 8, 9],
            xiao: [0, 1, 2, 3, 4],
            dan: [1, 3, 5, 7, 9],
            shuang: [0, 2, 4, 6, 8],
            zhi: [1, 2, 3, 5, 7],
            he: [0, 4, 6, 8, 9]
        },
        pk10: {
            num: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
            num_len: 10,
            col_td: 9,
            open_len: 10,
            zj_val: 5,
            max: 10,
            da: [6, 7, 8, 9, 10],
            xiao: [1, 2, 3, 4, 5],
            dan: [1, 3, 5, 7, 9],
            shuang: [2, 4, 6, 8, 10]
        },
        klsf: {
            num: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20],
            num_len: 20,
            col_td: 9,
            open_len: 8,
            zj_val: 10,
            max: 20,
            da: [11, 12, 13, 14, 15, 16, 17, 18, 19, 20],
            xiao: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
            dan: [1, 3, 5, 7, 9, 11, 13, 15, 17, 19],
            shuang: [2, 4, 6, 8, 10, 12, 14, 16, 18, 20],
            dong: [1, 5, 9, 13, 17],
            nan: [2, 6, 10, 14, 18],
            xi: [3, 7, 11, 15, 19],
            bei: [4, 8, 12, 16, 20],
            zhong: [1, 2, 3, 4, 5, 6, 7],
            fa: [8, 9, 10, 11, 12, 13, 14],
            bai: [15, 16, 17, 18, 19, 20]
        }
    };
    return app = {
        czinfo: {
            pk10: t.extend({}, e.pk10),
            speed10: t.extend({}, e.pk10),
            jspk10: t.extend({}, e.pk10),
			mspk10: t.extend({}, e.pk10),
			msft: t.extend({}, e.pk10),
            xyft: t.extend({}, e.pk10),
            jsft: t.extend({}, e.pk10),
            cqssc: t.extend({}, e.ssc),
            msssc: t.extend({}, e.ssc),
            speed5: t.extend({}, e.ssc),
            jsssc: t.extend({}, e.ssc),
            gdkl10: t.extend({}, e.klsf),
            xync: t.extend({}, e.klsf),
            tjssc: t.extend({}, e.ssc),
            xjssc: t.extend({}, e.ssc),
            jsk3: {
                num: [1, 2, 3, 4, 5, 6],
                num_len: 6,
                col_td: 9,
                open_len: 3,
                zj_val: 3,
                max: 6
            }
        },
        reload: !1,
        trend: {
            arr: [],
            category: []
        },
        sort: function(t, e) {
            return t - e
        },
        geturlstr: function(t) {
            var e = new RegExp("(^|&)" + t + "=([^&]*)(&|$)","i")
              , a = window.location.search.substr(1).match(e);
            return null != a ? decodeURIComponent(a[2]) : null
        },
        iframe_dump: function() {
            if (self != top && window.location.href.indexOf("base") < 0) {
                Math.random().toFixed(1) >= .5 && (window.parent.location.href = window.location.href)
            }
        },
        tab: function(e, a, n) {
            t(document).on(n, e, function() {
                t(this).addClass("active").siblings(e).removeClass("active"),
                t(a).eq(t(this).index()).show().siblings(a).hide()
            }),
            t(e).eq(0).trigger(n)
        },
        add_class: function(e, a) {
            t(e).addClass(a)
        },


        reset_active: function(e) {
            var a = "";
            e && "type" == e ? a = t(".btn-filter-type a") : e && "num" == e ? a = t(".btn-filter-num a") : e && "all" == e && (a = t(".btn-filter-num a,.btn-filter-type a")),
            "" != a && a.removeClass("active"),
            t(".history-table .td-num span").removeClass("opacity")
        },
        get_common_nums: function(e) {
            for (var a = {}, n = e.length, s = "", d = 0; d < n; d++)
                for (var r = 0, i = e[d].length; r < i; r++)
                    a[e[d][r]] ? a[e[d][r]]++ : a[e[d][r]] = 1,
                    a[e[d][r]] == n && (s += ".n" + e[d][r] + ",");
            s = s.substring(0, s.length - 1),
            t(s).removeClass("opacity")
        },
        nums_str: function(t, e, a) {
            var n = "";
            if ("dx" == a)
                for (var s = 0, d = e.length; s < d; s++)
                    n += '<span class="n' + e[s] + " n-" + (parseInt(e[s]) > app.czinfo[t].zj_val ? "da" : "xiao") + '" data-num="' + e[s] + '">' + e[s] + "</span>";
            else if ("ds" == a)
                for (var s = 0, d = e.length; s < d; s++)
                    n += '<span class="n' + e[s] + " n-" + (parseInt(e[s]) % 2 == 0 ? "shuang" : "dan") + '">' + e[s] + "</span>";
            else
                for (var s = 0, d = e.length; s < d; s++)
                    n += '<span class="n' + e[s] + '" data-num="' + e[s] + '">' + e[s] + "</span>";
            return n
        },
        nums_str_dx: function(t,e) {
            var n = "";
            
                for (var s = 0, d = e.length; s < d; s++)
                    n += '<span class="n-' + (parseInt(e[s]) > app.czinfo[t].zj_val ? "da" : "xiao") + '">' + (parseInt(e[s]) > app.czinfo[t].zj_val ? "澶�" : "灏�") + '</span>';
       
            return n
        },
        nums_str_ds: function(t,e) {
            var n = "";
            
                for (var s = 0, d = e.length; s < d; s++)
                    n += '<span class="n-' + (parseInt(e[s]) % 2 == 0 ? "shuang" : "dan") + '">' + (parseInt(e[s]) % 2 == 0 ? "鍙�" : "鍗�") + "</span>";
       
            return n
        },
        nums_str_lh: function(t,e) {
            var n = "";
            
                for (var s = 0, d = e.length; s < d; s++)
		
                    n += '<span class="n-' + (e[s]== '铏�' ? "hu" : "long") + '">' + e[s] + "</span>";
       
            return n
        },
		
        drawing: function(e) {
            clearInterval(app.czinfo[e].open_timer);
            var a = app.czinfo[e].open_len
              , n = app.czinfo[e].num_len
              , s = app.czinfo[e].num;
            app.czinfo[e].open_timer = setInterval(function() {
                for (var d = "", r = 0; r < a; r++) {
                    var i = Math.floor(Math.random() * n);
                    d += '<span class="n' + s[i] + '">' + s[i] + "</span>"
                }
                t("#" + e).find(".cai-num").html(d),
                t("#" + e).find(".djs").html("<font color=red>姝ｅ湪寮€濂�</font>")
            }, 110)
        },
        get_lottery: function(e) {
            app.drawing(e);
            var a = (new Date).getTime();
            t.ajax({
                url: "/api/newest?code=" + e + "&t=" + a,
                method: "get",
                success: function(t) {
                    if (0 === t.code) {
                        var a = t.data
                          , n = a.current - a.newest.issue;
                        app.czinfo[e].ticking = a.ticking,
                        n > 2 || 1 == n || 0 == n || !a.newest.issue ? (app.countdown(e),
                        clearInterval(app.czinfo[e].open_timer),
                        app.lottery_result(e, a)) : (app.plan_draw(e, a),
                        app.get_retry(e),
                        app.reload = !0)
						try {
                            // 澧炲姞瀵硅棰戠殑璋冪敤
                            if (app.video)
								
                                app.video.gotLotteryData(e, a);
                        }
                        catch (err) {
                            console.log(err)
                        }
                    } else
                        app.get_retry(e)
                }
            })
        },
        get_retry: function(t) {
            clearTimeout(app.czinfo[t].timeout),
            app.czinfo[t].timeout = setTimeout(function() {
                app.get_lottery(t)
            }, 1e3)
        },
        plan_draw: function(e, a) {
            t("#" + e).find(".current,.next").html(a.newest.issue + 1),
            t("#" + e).find(".open").html(a.currentNo),
            t("#" + e).find(".period-leave").html(a.remain)
        },
        countdown: function(e) {
            t("#" + e).find(".djs").html(app.format_time(app.czinfo[e].ticking, "<font color=red>姝ｅ湪寮€濂�</font>")),
            clearInterval(app.czinfo[e].timer),
            app.czinfo[e].timer = setInterval(function() {
                if (--app.czinfo[e].ticking < 0)
                    return clearInterval(app.czinfo[e].timer),
                    void app.get_lottery(e);
                t("#" + e).find(".djs").html(app.format_time(app.czinfo[e].ticking, "<font color=red>姝ｅ湪寮€濂�</font>"))
            }, 1e3)
        },
        lottery_result: function(e, a) {
            t("#" + e).find(".current").html(a.newest.issue ? a.newest.issue : a.current),
            t("#" + e).find(".open").html(a.currentNo - 1),
            t("#" + e).find(".period-leave").html(a.remain),
            t("#" + e).find(".next").html(a.current);
            var n = a.newest.array ? app.nums_str(e, a.newest.array) : "绛夊緟寮€濂�";
            t("#" + e).find(".cai-num").html(n),
			t("#" + e).find(".summery").html(a.summery.join(' ')),
		
            app.reload && (
			refresh()
			)
            app.reload = !0
        },





        get_countdown: function(e,n) {
			
            var a = (new Date).getTime();
            t.ajax({
                url: "/api/newest?code=" + e + "&t=" + a,
                method: "get",
                success: function(t) {
                    0 === t.code && app.live_djs(e, t.data.ticking);
                }
            })
        },
        live_djs: function(e, a) {
            app.czinfo[e].live_ticking = a,
            t("#" + e).find(".second").html(a),
            clearInterval(app.czinfo[e].live_timer),
            app.czinfo[e].live_timer = setInterval(function() {
                if (--app.czinfo[e].live_ticking < 0)
                    return clearInterval(app.czinfo[e].live_timer),
                    void app.get_countdown(e,1);
                t("#" + e).find(".second").html(app.czinfo[e].live_ticking)
            }, 1e3)
        },



        loadmark: function(e) {
            var a = {
                text: "",
                time: 1e3,
                img: !1,
                callback: ""
            };
            t.extend(a, e),
            t("body").find(".loadmark").remove();
            var n = "";
            a.img && (n = '<p class="load_gif"><img src="/static/images/loading.gif" width="100%" /></p>');
            var s = '<div class="loadmark"><div class="load_tip">' + n + t.trim(a.text) + "</div></div>";
            t("body").append(s);
            var d = t("body").find(".loadmark");
            if (d.find(".load_tip").css({
                "margin-left": -(d.find(".load_tip").outerWidth() / 2),
                "margin-top": -(d.find(".load_tip").outerHeight() / 2)
            }),
            a.time > 0)
                var r = null
                  , r = setTimeout(function() {
                    d.remove(),
                    "" != a.callback && a.callback(),
                    clearTimeout(r)
                }, a.time)
        },
        closemark: function() {
            t(".loadmark").remove()
        },
        date_arr: function() {
            var e = new Date;
            e.setDate(e.getDate() - 14);
            for (var t, n = [], i = 0; i < 15; i++)
                t = e.getFullYear() + "-" + (e.getMonth() + 1 < 10 ? "0" + (e.getMonth() + 1) : e.getMonth() + 1) + "-" + (e.getDate() < 10 ? "0" + e.getDate() : e.getDate()),
                n.push(t),
                e.setDate(e.getDate() + 1);
            return n.reverse()
        },
        get_date: function(t) {
            var e = new Date;
            if (!(!e instanceof Date)) {
                var a = {
                    yyyy: e.getFullYear(),
                    M: e.getMonth() + 1,
                    d: e.getDate(),
                    H: e.getHours(),
                    m: e.getMinutes(),
                    s: e.getSeconds(),
                    MM: ("" + (e.getMonth() + 101)).substr(1),
                    dd: ("" + (e.getDate() + 100)).substr(1),
                    HH: ("" + (e.getHours() + 100)).substr(1),
                    mm: ("" + (e.getMinutes() + 100)).substr(1),
                    ss: ("" + (e.getSeconds() + 100)).substr(1)
                };
                return t.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function() {
                    return a[arguments[0]]
                })
            }
        },
        format_time: function(t, e) {
            var a = parseFloat(t)
              , n = 0
              , s = 0
              , d = 0
              , r = "<em>鍒�</em>"
              , i = "</span>";
            return null != a && "" != a && (a < 60 ? (d = a,
            a = "<span>00</span>" + r + "<span>" + (d > 9 ? d : "0" + d) + i + "<em>绉�</em>") : a >= 60 && a < 3600 ? (s = parseInt(a / 60),
            d = parseInt(a % 60),
            a = "<span>" + (s > 9 ? s : "0" + s) + i + r + "<span>" + (d > 9 ? d : "0" + d) + i + "<em>绉�</em>") : a >= 3600 && (n = parseInt(a / 3600),
            s = parseInt(a % 3600 / 60),
            d = parseInt(a % 3600 % 60 % 60),
            a = "<span>" + (n > 9 ? n : "0" + n) + i + "<em>鏃�</em><span>" + (s > 9 ? s : "0" + s) + i + r)),
            0 == a && e ? e : a
        },
        format_djs: function(time) {
	
			var h=Math.floor(time/3600);
			var i=Math.floor(time/60) % 60;
			var s=time%60;	
			time = '<b>'+h+'</b><ins>鏃�</ins><b>'+i+'</b><ins>鍒�</ins><b>'+s+'</b><ins>绉�</ins>';
			return time;  
        },
        tc: function(t, e) {
            return 1 == app.unique_len(e) ? "閫氬悆" : t > 10 ? '<span class="c-red">澶�</span>' : "灏�"
        },
        lh: function(t, e, a) {
            var n = parseInt(t)
              , s = parseInt(e);
            return n > s ? "l" == a ? '<span class="c-red">榫�</span>' : "榫�" : n == s ? "鍜�" : "h" == a ? '<span class="c-red">铏�</span>' : "铏�"
        },
        ds: function(t, e) {
            return t % 2 == 0 ? "s" == e ? '<span class="c-red">鍙�</span>' : "鍙�" : "d" == e ? '<span class="c-red">鍗�</span>' : "鍗�"
        },
        dx: function(t, e, a) {
            var n = parseInt(t)
              , s = parseInt(e);
            return n > s ? "d" == a ? '<span class="c-red">澶�</span>' : "澶�" : n == s ? "鍜�" : "x" == a ? '<span class="c-red">灏�</span>' : "灏�"
        },
        unique_len: function(e) {
            return t.unique(e).length
        },
        shun: function(t) {
            var e = t.sort(app.sort)
              , a = 0;
            if (1 == app.unique_len(t))
                return "璞瑰瓙";
            if (2 == app.unique_len(t))
                return "瀵瑰瓙";
            for (var n = 0, s = e.length - 1; n < s; n++)
                parseInt(e[n]) + 1 == e[n + 1] && a++;
            return a == e.length - 1 ? "椤哄瓙" : a > 0 ? "鍗婇『" : "鏉傚叚"
        },
        date_format: function(t, e) {
            if (t) {
                switch (e || (e = "yyyy-MM-dd"),
                t.indexOf("-") > -1 && (t = t.replace(/-/g, "/")),
                typeof t) {
                case "string":
                    t = new Date(t.replace(/-/g, "/"));
                    break;
                case "number":
                    t = new Date(t)
                }
                if (!(!t instanceof Date)) {
                    var a = {
                        yyyy: t.getFullYear(),
                        M: t.getMonth() + 1,
                        d: t.getDate(),
                        H: t.getHours(),
                        m: t.getMinutes(),
                        s: t.getSeconds(),
                        MM: ("" + (t.getMonth() + 101)).substr(1),
                        dd: ("" + (t.getDate() + 100)).substr(1),
                        HH: ("" + (t.getHours() + 100)).substr(1),
                        mm: ("" + (t.getMinutes() + 100)).substr(1),
                        ss: ("" + (t.getSeconds() + 100)).substr(1)
                    };
                    return e.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function() {
                        return a[arguments[0]]
                    })
                }
            }
        },
        time: function(t) {
            var e = parseFloat(t)
              , a = 0
              , n = 0
              , s = 0;
            return null != e && (e < 60 ? (s = e,
            e = "00:" + (s > 9 ? s : "0" + s)) : e >= 60 && e < 3600 ? (n = parseInt(e / 60),
            s = parseInt(e % 60),
            e = (n > 9 ? n : "0" + n) + ":" + (s > 9 ? s : "0" + s)) : e >= 3600 && (a = parseInt(e / 3600),
            n = parseInt(e % 3600 / 60),
            s = parseInt(e % 3600 % 60 % 60),
            e = (a > 9 ? a : "0" + a) + ":" + (n > 9 ? n : "0" + n) + ":" + (s > 9 ? s : "0" + s))),
            e
        },



		startVideo: function() {
			$("#videobox").animate({
				"z-index": "19999"
			}, 10, function() {
				var W = $(".animate").width();
				var H = W * 880 / 1310;
				$(".content").css({
					"height": H + 35
				});
				$(".content").animate({
					"top": "0"
				}, 500, function() {
					//app.iframe(); //鍔犺浇鍔ㄧ敾鐣岄潰
				
				});
			});
		}
    }
});