// 存放主要交互逻辑的js代码
// javascript 模块化
var seckill = {
    // 封装秒杀相关ajax的url
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + "/" + md5 + "/execution";
        },
    },
    handlerSeckillKill: function (seckillId, node) {
        // 获取秒杀逻辑,控制实现逻辑,执行秒杀
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            // 在回调函数中,执行交互流程
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    // 开启秒杀
                    // 获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log('killURl:' + killUrl);
                    // 绑定一次点击事件
                    $('#killBtn').one('click', function () {
                        // 执行秒杀请求
                        // 1: 先禁用按钮
                        $(this).addClass('disables');
                        // 2: 发送秒杀请求
                        $.post(killUrl, {}, function (result) {
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                // 显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                } else {
                    // 未开启秒杀
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    // 重新计算计时逻辑
                    seckill.countdown(seckillId, now, start, end);
                }
            } else {
                console.log('result:' + result);
            }
        })
    },
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    countdown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        // 时间判断
        if (nowTime > endTime) {
            // 秒杀结束
            seckillBox.html('秒杀结束');
        } else if (nowTime < startTime) {
            // 秒杀未开始,计时
            var killTime = new Date(startTime + 1000);

            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
                seckillBox.html(format);
                /*时间完成后回调事件*/
            }).on('finish.countdown', function () {
                seckill.handlerSeckillKill(seckillId, seckillBox);
            });
        } else {
            // 秒杀开始
            seckill.handlerSeckillKill(seckillId, seckillBox);
        }
    },
    // 详情页秒杀逻辑
    detail: {
        // 详情页初始化
        init: function (params) {
            // 手机验证和登录,计时交互
            // 规划交互流程

            // var killPhone = $.cookie('killPhone');    // TODO
            var userId = $.cookie('USERID');

            var startTime = params['startTime'];
            var seckillId = params['seckillId'];
            var endTime = params['endTime'];
            if (userId == null) {
                // 控制输出
                var killPhoneModal = $('#killPhoneModal');
                // 显示弹出层
                killPhoneModal.modal({
                    show: true, // 显示弹出层
                    backdrop: 'static', //禁止位置关闭
                    keyboard: false // 关闭键盘事件
                });
                $('#killMessage').hide().html('<label class="label label-danger">尚未登录,请登录</label>').show(300);
                $('#killPhoneBtn').click(function () {
                    window.location.href = "/seckill/login";
                });
            }

            // 已经登录
            // 计时交互
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];
                    // 时间判断
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result' + result);
                }

            });

        },
    },
    judge_login: function () {
        // 判断浏览器中是否保存了cookie
        var userId = $.cookie('USERID');
        console.log(userId);
        if (userId != null) {
            $('#per-center').removeClass("hidden");
            $('#need-login').addClass("hidden");
        } else {
            $('#need-login').removeClass("hidden");
            $('#per-center').addClass("hidden");
        }
    },
    loadValidateCode: function () {
        var time = new Date().getTime();
        $("#validateCodeImage").attr('src', '/seckill/loadValidateCode')
    },
//    验证表单是否输入验证码用户名以及密码
    interceptform: function () {
        $('form').submit(function (e) {
            var node = $('#errorTips');
            var phone = $('#phone').val();
            var password = $('#password').val();
            if (phone == null || password == null || password.length < 3) {
                node.hide().html('<label class="label label-danger">用户名或密码填写格式错误</label>').show(300);
                e.preventDefault();
            }
            var validateCode = $('#validateCode').val();
            if (validateCode == null || validateCode.length != 4) {
                node.hide().html('<label class="label label-danger">验证码格式错误</label>').show(300);
                e.preventDefault();
            }
        });
    },
}