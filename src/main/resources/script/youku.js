function s(t) {
    function e(t, e) {
        return t << e | t >>> 32 - e
    }
    function n(t, e) {
        var n, i, r, a, o;
        return r = 2147483648 & t, a = 2147483648 & e, n = 1073741824 & t, i = 1073741824 & e, o = (1073741823 & t) + (1073741823 & e), n & i ? 2147483648 ^ o ^ r ^ a : n | i ? 1073741824 & o ? 3221225472 ^ o ^ r ^ a : 1073741824 ^ o ^ r ^ a : o ^ r ^ a
    }
    function i(t, e, n) {
        return t & e | ~t & n
    }
    function r(t, e, n) {
        return t & n | e & ~n
    }
    function a(t, e, n) {
        return t ^ e ^ n
    }
    function o(t, e, n) {
        return e ^ (t | ~n)
    }
    function s(t, r, a, o, s, l, u) {
        return t = n(t, n(n(i(r, a, o), s), u)), n(e(t, l), r)
    }
    function l(t, i, a, o, s, l, u) {
        return t = n(t, n(n(r(i, a, o), s), u)), n(e(t, l), i)
    }
    function u(t, i, r, o, s, l, u) {
        return t = n(t, n(n(a(i, r, o), s), u)), n(e(t, l), i)
    }
    function c(t, i, r, a, s, l, u) {
        return t = n(t, n(n(o(i, r, a), s), u)), n(e(t, l), i)
    }
    function d(t) {
        var e, n, i = "",
            r = "";
        for (n = 0; 3 >= n; n++) e = t >>> 8 * n & 255, r = "0" + e.toString(16), i += r.substr(r.length - 2, 2);
        return i
    }
    var h, f, p, g, v, y, m, _, b, E = [];
    for (t = function(t) {
        t = t.replace(/\r\n/g, "\n");
        for (var e = "", n = 0; n < t.length; n++) {
            var i = t.charCodeAt(n);
            128 > i ? e += String.fromCharCode(i) : i > 127 && 2048 > i ? (e += String.fromCharCode(i >> 6 | 192), e += String.fromCharCode(63 & i | 128)) : (e += String.fromCharCode(i >> 12 | 224), e += String.fromCharCode(i >> 6 & 63 | 128), e += String.fromCharCode(63 & i | 128))
        }
        return e
    }(t), E = function(t) {
        for (var e, n = t.length, i = n + 8, r = (i - i % 64) / 64, a = 16 * (r + 1), o = new Array(a - 1), s = 0, l = 0; n > l;) e = (l - l % 4) / 4, s = l % 4 * 8, o[e] = o[e] | t.charCodeAt(l) << s, l++;
        return e = (l - l % 4) / 4, s = l % 4 * 8, o[e] = o[e] | 128 << s, o[a - 2] = n << 3, o[a - 1] = n >>> 29, o
    }(t), y = 1732584193, m = 4023233417, _ = 2562383102, b = 271733878, h = 0; h < E.length; h += 16) f = y, p = m, g = _, v = b, y = s(y, m, _, b, E[h + 0], 7, 3614090360), b = s(b, y, m, _, E[h + 1], 12, 3905402710), _ = s(_, b, y, m, E[h + 2], 17, 606105819), m = s(m, _, b, y, E[h + 3], 22, 3250441966), y = s(y, m, _, b, E[h + 4], 7, 4118548399), b = s(b, y, m, _, E[h + 5], 12, 1200080426), _ = s(_, b, y, m, E[h + 6], 17, 2821735955),
        m = s(m, _, b, y, E[h + 7], 22, 4249261313), y = s(y, m, _, b, E[h + 8], 7, 1770035416), b = s(b, y, m, _, E[h + 9], 12, 2336552879), _ = s(_, b, y, m, E[h + 10], 17, 4294925233), m = s(m, _, b, y, E[h + 11], 22, 2304563134), y = s(y, m, _, b, E[h + 12], 7, 1804603682), b = s(b, y, m, _, E[h + 13], 12, 4254626195), _ = s(_, b, y, m, E[h + 14], 17, 2792965006), m = s(m, _, b, y, E[h + 15], 22, 1236535329), y = l(y, m, _, b, E[h + 1], 5, 4129170786), b = l(b, y, m, _, E[h + 6], 9, 3225465664), _ = l(_, b, y, m, E[h + 11], 14, 643717713), m = l(m, _, b, y, E[h + 0], 20, 3921069994), y = l(y, m, _, b, E[h + 5], 5, 3593408605), b = l(b, y, m, _, E[h + 10], 9, 38016083), _ = l(_, b, y, m, E[h + 15], 14, 3634488961), m = l(m, _, b, y, E[h + 4], 20, 3889429448), y = l(y, m, _, b, E[h + 9], 5, 568446438), b = l(b, y, m, _, E[h + 14], 9, 3275163606), _ = l(_, b, y, m, E[h + 3], 14, 4107603335), m = l(m, _, b, y, E[h + 8], 20, 1163531501), y = l(y, m, _, b, E[h + 13], 5, 2850285829), b = l(b, y, m, _, E[h + 2], 9, 4243563512), _ = l(_, b, y, m, E[h + 7], 14, 1735328473), m = l(m, _, b, y, E[h + 12], 20, 2368359562), y = u(y, m, _, b, E[h + 5], 4, 4294588738), b = u(b, y, m, _, E[h + 8], 11, 2272392833), _ = u(_, b, y, m, E[h + 11], 16, 1839030562), m = u(m, _, b, y, E[h + 14], 23, 4259657740), y = u(y, m, _, b, E[h + 1], 4, 2763975236), b = u(b, y, m, _, E[h + 4], 11, 1272893353), _ = u(_, b, y, m, E[h + 7], 16, 4139469664), m = u(m, _, b, y, E[h + 10], 23, 3200236656), y = u(y, m, _, b, E[h + 13], 4, 681279174), b = u(b, y, m, _, E[h + 0], 11, 3936430074), _ = u(_, b, y, m, E[h + 3], 16, 3572445317), m = u(m, _, b, y, E[h + 6], 23, 76029189), y = u(y, m, _, b, E[h + 9], 4, 3654602809), b = u(b, y, m, _, E[h + 12], 11, 3873151461), _ = u(_, b, y, m, E[h + 15], 16, 530742520), m = u(m, _, b, y, E[h + 2], 23, 3299628645), y = c(y, m, _, b, E[h + 0], 6, 4096336452), b = c(b, y, m, _, E[h + 7], 10, 1126891415), _ = c(_, b, y, m, E[h + 14], 15, 2878612391), m = c(m, _, b, y, E[h + 5], 21, 4237533241), y = c(y, m, _, b, E[h + 12], 6, 1700485571), b = c(b, y, m, _, E[h + 3], 10, 2399980690), _ = c(_, b, y, m, E[h + 10], 15, 4293915773), m = c(m, _, b, y, E[h + 1], 21, 2240044497), y = c(y, m, _, b, E[h + 8], 6, 1873313359), b = c(b, y, m, _, E[h + 15], 10, 4264355552), _ = c(_, b, y, m, E[h + 6], 15, 2734768916), m = c(m, _, b, y, E[h + 13], 21, 1309151649), y = c(y, m, _, b, E[h + 4], 6, 4149444226), b = c(b, y, m, _, E[h + 11], 10, 3174756917), _ = c(_, b, y, m, E[h + 2], 15, 718787259), m = c(m, _, b, y, E[h + 9], 21, 3951481745), y = n(y, f), m = n(m, p), _ = n(_, g), b = n(b, v);
    return (d(y) + d(m) + d(_) + d(b)).toLowerCase()
}