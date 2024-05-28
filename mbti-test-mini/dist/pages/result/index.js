"use strict";
(wx["webpackJsonp"] = wx["webpackJsonp"] || []).push([["pages/result/index"],{

/***/ "./node_modules/@tarojs/taro-loader/lib/entry-cache.js?name=pages/result/index!./src/pages/result/index.tsx":
/*!******************************************************************************************************************!*\
  !*** ./node_modules/@tarojs/taro-loader/lib/entry-cache.js?name=pages/result/index!./src/pages/result/index.tsx ***!
  \******************************************************************************************************************/
/***/ (function(__unused_webpack_module, __webpack_exports__, __webpack_require__) {

/* harmony import */ var _tarojs_components__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! @tarojs/components */ "./node_modules/@tarojs/plugin-platform-weapp/dist/components-react.js");
/* harmony import */ var taro_ui__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! taro-ui */ "webpack/container/remote/taro-ui");
/* harmony import */ var taro_ui__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(taro_ui__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var _tarojs_taro__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @tarojs/taro */ "webpack/container/remote/@tarojs/taro");
/* harmony import */ var _tarojs_taro__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(_tarojs_taro__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var _assets_headerBg_jpg__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../../assets/headerBg.jpg */ "./src/assets/headerBg.jpg");
/* harmony import */ var _components_GlobalFooter__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../../components/GlobalFooter */ "./src/components/GlobalFooter/index.tsx");
/* harmony import */ var _utils_bizUtils__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ../../utils/bizUtils */ "./src/utils/bizUtils.ts");
/* harmony import */ var _data_questions_json__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ../../data/questions.json */ "./src/data/questions.json");
/* harmony import */ var _data_question_results_json__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ../../data/question_results.json */ "./src/data/question_results.json");
/* harmony import */ var react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! react/jsx-runtime */ "webpack/container/remote/react/jsx-runtime");
/* harmony import */ var react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7___default = /*#__PURE__*/__webpack_require__.n(react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7__);










/**
 * 测试结果页面
 */

/* harmony default export */ __webpack_exports__["default"] = (function () {
  // 获取答案
  var answerList = _tarojs_taro__WEBPACK_IMPORTED_MODULE_1___default().getStorageSync("answerList");
  if (!answerList || answerList.length < 1) {
    _tarojs_taro__WEBPACK_IMPORTED_MODULE_1___default().showToast({
      title: "答案为空",
      icon: "error",
      duration: 3000
    });
  }
  // 获取测试结果
  var result = (0,_utils_bizUtils__WEBPACK_IMPORTED_MODULE_4__.getBestQuestionResult)(answerList, _data_questions_json__WEBPACK_IMPORTED_MODULE_5__, _data_question_results_json__WEBPACK_IMPORTED_MODULE_6__);
  return /*#__PURE__*/(0,react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7__.jsxs)(_tarojs_components__WEBPACK_IMPORTED_MODULE_8__.View, {
    className: "resultPage",
    children: [/*#__PURE__*/(0,react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7__.jsx)(_tarojs_components__WEBPACK_IMPORTED_MODULE_8__.View, {
      className: "at-article__h1 title",
      children: result.resultName
    }), /*#__PURE__*/(0,react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7__.jsx)(_tarojs_components__WEBPACK_IMPORTED_MODULE_8__.View, {
      className: "at-article__h2 subTitle",
      children: result.resultDesc
    }), /*#__PURE__*/(0,react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7__.jsx)(taro_ui__WEBPACK_IMPORTED_MODULE_0__.AtButton, {
      type: "primary",
      circle: true,
      className: "enterBtn",
      onClick: function onClick() {
        _tarojs_taro__WEBPACK_IMPORTED_MODULE_1___default().reLaunch({
          url: "/pages/index/index"
        });
      },
      children: "\u8FD4\u56DE\u4E3B\u9875"
    }), /*#__PURE__*/(0,react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7__.jsx)(_tarojs_components__WEBPACK_IMPORTED_MODULE_8__.Image, {
      className: "headerBg",
      src: _assets_headerBg_jpg__WEBPACK_IMPORTED_MODULE_2__
    }), /*#__PURE__*/(0,react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7__.jsx)(_components_GlobalFooter__WEBPACK_IMPORTED_MODULE_3__["default"], {})]
  });
});

/***/ }),

/***/ "./src/pages/result/index.tsx":
/*!************************************!*\
  !*** ./src/pages/result/index.tsx ***!
  \************************************/
/***/ (function(__unused_webpack_module, __unused_webpack___webpack_exports__, __webpack_require__) {

/* harmony import */ var _tarojs_runtime__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @tarojs/runtime */ "webpack/container/remote/@tarojs/runtime");
/* harmony import */ var _tarojs_runtime__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_tarojs_runtime__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var _node_modules_tarojs_taro_loader_lib_entry_cache_js_name_pages_result_index_index_tsx__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! !!../../../node_modules/@tarojs/taro-loader/lib/entry-cache.js?name=pages/result/index!./index.tsx */ "./node_modules/@tarojs/taro-loader/lib/entry-cache.js?name=pages/result/index!./src/pages/result/index.tsx");


var config = {"navigationBarTitleText":"查看结果"};


var inst = Page((0,_tarojs_runtime__WEBPACK_IMPORTED_MODULE_0__.createPageConfig)(_node_modules_tarojs_taro_loader_lib_entry_cache_js_name_pages_result_index_index_tsx__WEBPACK_IMPORTED_MODULE_1__["default"], 'pages/result/index', {root:{cn:[]}}, config || {}))


/* unused harmony default export */ var __WEBPACK_DEFAULT_EXPORT__ = (_node_modules_tarojs_taro_loader_lib_entry_cache_js_name_pages_result_index_index_tsx__WEBPACK_IMPORTED_MODULE_1__["default"]);


/***/ }),

/***/ "./src/utils/bizUtils.ts":
/*!*******************************!*\
  !*** ./src/utils/bizUtils.ts ***!
  \*******************************/
/***/ (function(__unused_webpack_module, __webpack_exports__, __webpack_require__) {

/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   getBestQuestionResult: function() { return /* binding */ getBestQuestionResult; }
/* harmony export */ });
/* harmony import */ var D_MyCode_PlanetProjects_maidada_mbti_test_mini_node_modules_babel_runtime_helpers_esm_createForOfIteratorHelper_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./node_modules/@babel/runtime/helpers/esm/createForOfIteratorHelper.js */ "./node_modules/@babel/runtime/helpers/esm/createForOfIteratorHelper.js");

/**
 * 获取最佳题目评分结果
 * @param answerList
 * @param questions
 * @param question_results
 */

function getBestQuestionResult(answerList, questions, question_results) {
  // 初始化一个对象，用于存储每个选项的计数
  var optionCount = {};

  // 用户选择 A, B, C
  // 对应 result：I, I, J
  // optionCount[I] = 2; optionCount[J] = 1

  // 遍历题目列表
  var _iterator = (0,D_MyCode_PlanetProjects_maidada_mbti_test_mini_node_modules_babel_runtime_helpers_esm_createForOfIteratorHelper_js__WEBPACK_IMPORTED_MODULE_0__["default"])(questions),
    _step;
  try {
    for (_iterator.s(); !(_step = _iterator.n()).done;) {
      var question = _step.value;
      // 遍历答案列表
      var _iterator3 = (0,D_MyCode_PlanetProjects_maidada_mbti_test_mini_node_modules_babel_runtime_helpers_esm_createForOfIteratorHelper_js__WEBPACK_IMPORTED_MODULE_0__["default"])(answerList),
        _step3;
      try {
        for (_iterator3.s(); !(_step3 = _iterator3.n()).done;) {
          var answer = _step3.value;
          // 遍历题目中的选项
          var _iterator4 = (0,D_MyCode_PlanetProjects_maidada_mbti_test_mini_node_modules_babel_runtime_helpers_esm_createForOfIteratorHelper_js__WEBPACK_IMPORTED_MODULE_0__["default"])(question.options),
            _step4;
          try {
            for (_iterator4.s(); !(_step4 = _iterator4.n()).done;) {
              var option = _step4.value;
              // 如果答案和选项的key匹配
              if (option.key === answer) {
                // 获取选项的result属性
                var result = option.result;

                // 如果result属性不在optionCount中，初始化为0
                if (!optionCount[result]) {
                  optionCount[result] = 0;
                }

                // 在optionCount中增加计数
                optionCount[result]++;
              }
            }
          } catch (err) {
            _iterator4.e(err);
          } finally {
            _iterator4.f();
          }
        }
      } catch (err) {
        _iterator3.e(err);
      } finally {
        _iterator3.f();
      }
    }

    // 初始化最高分数和最高分数对应的评分结果
  } catch (err) {
    _iterator.e(err);
  } finally {
    _iterator.f();
  }
  var maxScore = 0;
  var maxScoreResult = question_results[0];

  // 遍历评分结果列表
  var _iterator2 = (0,D_MyCode_PlanetProjects_maidada_mbti_test_mini_node_modules_babel_runtime_helpers_esm_createForOfIteratorHelper_js__WEBPACK_IMPORTED_MODULE_0__["default"])(question_results),
    _step2;
  try {
    for (_iterator2.s(); !(_step2 = _iterator2.n()).done;) {
      var _result = _step2.value;
      // 计算当前评分结果的分数
      var score = _result.resultProp.reduce(function (count, prop) {
        return count + (optionCount[prop] || 0);
      }, 0);

      // 如果分数高于当前最高分数，更新最高分数和最高分数对应的评分结果
      if (score > maxScore) {
        maxScore = score;
        maxScoreResult = _result;
      }
    }

    // 返回最高分数和最高分数对应的评分结果
  } catch (err) {
    _iterator2.e(err);
  } finally {
    _iterator2.f();
  }
  return maxScoreResult;
}

// 示例数据
var answerList = ["B", "B", "B", "A"];
var questions = [{
  title: "你通常更喜欢",
  options: [{
    result: "I",
    value: "独自工作",
    key: "A"
  }, {
    result: "E",
    value: "与他人合作",
    key: "B"
  }]
}, {
  options: [{
    result: "S",
    value: "喜欢有结构和常规",
    key: "A"
  }, {
    result: "N",
    value: "喜欢自由和灵活性",
    key: "B"
  }],
  title: "对于日常安排"
}, {
  options: [{
    result: "P",
    value: "首先考虑可能性",
    key: "A"
  }, {
    result: "J",
    value: "首先考虑后果",
    key: "B"
  }],
  title: "当遇到问题时"
}, {
  options: [{
    result: "T",
    value: "时间是一种宝贵的资源",
    key: "A"
  }, {
    result: "F",
    value: "时间是相对灵活的概念",
    key: "B"
  }],
  title: "你如何看待时间"
}];
var question_results = [{
  resultProp: ["I", "S", "T", "J"],
  resultDesc: "忠诚可靠，被公认为务实，注重细节。",
  resultPicture: "icon_url_istj",
  resultName: "ISTJ（物流师）"
}, {
  resultProp: ["I", "S", "F", "J"],
  resultDesc: "善良贴心，以同情心和责任为特点。",
  resultPicture: "icon_url_isfj",
  resultName: "ISFJ（守护者）"
}];
console.log(getBestQuestionResult(answerList, questions, question_results));

/***/ }),

/***/ "./node_modules/@babel/runtime/helpers/esm/createForOfIteratorHelper.js":
/*!******************************************************************************!*\
  !*** ./node_modules/@babel/runtime/helpers/esm/createForOfIteratorHelper.js ***!
  \******************************************************************************/
/***/ (function(__unused_webpack___webpack_module__, __webpack_exports__, __webpack_require__) {

/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "default": function() { return /* binding */ _createForOfIteratorHelper; }
/* harmony export */ });
/* harmony import */ var _unsupportedIterableToArray_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./unsupportedIterableToArray.js */ "./node_modules/@babel/runtime/helpers/esm/unsupportedIterableToArray.js");

function _createForOfIteratorHelper(o, allowArrayLike) {
  var it = typeof Symbol !== "undefined" && o[Symbol.iterator] || o["@@iterator"];
  if (!it) {
    if (Array.isArray(o) || (it = (0,_unsupportedIterableToArray_js__WEBPACK_IMPORTED_MODULE_0__["default"])(o)) || allowArrayLike && o && typeof o.length === "number") {
      if (it) o = it;
      var i = 0;
      var F = function F() {};
      return {
        s: F,
        n: function n() {
          if (i >= o.length) return {
            done: true
          };
          return {
            done: false,
            value: o[i++]
          };
        },
        e: function e(_e) {
          throw _e;
        },
        f: F
      };
    }
    throw new TypeError("Invalid attempt to iterate non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.");
  }
  var normalCompletion = true,
    didErr = false,
    err;
  return {
    s: function s() {
      it = it.call(o);
    },
    n: function n() {
      var step = it.next();
      normalCompletion = step.done;
      return step;
    },
    e: function e(_e2) {
      didErr = true;
      err = _e2;
    },
    f: function f() {
      try {
        if (!normalCompletion && it["return"] != null) it["return"]();
      } finally {
        if (didErr) throw err;
      }
    }
  };
}

/***/ }),

/***/ "./src/data/question_results.json":
/*!****************************************!*\
  !*** ./src/data/question_results.json ***!
  \****************************************/
/***/ (function(module) {

module.exports = /*#__PURE__*/JSON.parse('[{"resultProp":["I","S","T","J"],"resultDesc":"忠诚可靠，被公认为务实，注重细节。","resultPicture":"icon_url_istj","resultName":"ISTJ（物流师）"},{"resultProp":["I","S","F","J"],"resultDesc":"善良贴心，以同情心和责任为特点。","resultPicture":"icon_url_isfj","resultName":"ISFJ（守护者）"},{"resultProp":["I","N","F","J"],"resultDesc":"理想主义者，有着深刻的洞察力，善于理解他人。","resultPicture":"icon_url_infj","resultName":"INFJ（占有者）"},{"resultProp":["I","N","T","J"],"resultDesc":"独立思考者，善于规划和实现目标，理性而果断。","resultPicture":"icon_url_intj","resultName":"INTJ（设计师）"},{"resultProp":["I","S","T","P"],"resultDesc":"冷静自持，善于解决问题，擅长实践技能。","resultPicture":"icon_url_istp","resultName":"ISTP（运动员）"},{"resultProp":["I","S","F","P"],"resultDesc":"具有艺术感和敏感性，珍视个人空间和自由。","resultPicture":"icon_url_isfp","resultName":"ISFP（艺术家）"},{"resultProp":["I","N","F","P"],"resultDesc":"理想主义者，富有创造力，以同情心和理解他人著称。","resultPicture":"icon_url_infp","resultName":"INFP（治愈者）"},{"resultProp":["I","N","T","P"],"resultDesc":"思维清晰，探索精神，独立思考且理性。","resultPicture":"icon_url_intp","resultName":"INTP（学者）"},{"resultProp":["E","S","T","P"],"resultDesc":"敢于冒险，乐于冒险，思维敏捷，行动果断。","resultPicture":"icon_url_estp","resultName":"ESTP（拓荒者）"},{"resultProp":["E","S","F","P"],"resultDesc":"热情开朗，善于社交，热爱生活，乐于助人。","resultPicture":"icon_url_esfp","resultName":"ESFP（表演者）"},{"resultProp":["E","N","F","P"],"resultDesc":"富有想象力，充满热情，善于激发他人的活力和潜力。","resultPicture":"icon_url_enfp","resultName":"ENFP（倡导者）"},{"resultProp":["E","N","T","P"],"resultDesc":"充满创造力，善于辩论，挑战传统，喜欢探索新领域。","resultPicture":"icon_url_entp","resultName":"ENTP（发明家）"},{"resultProp":["E","S","T","J"],"resultDesc":"务实果断，善于组织和管理，重视效率和目标。","resultPicture":"icon_url_estj","resultName":"ESTJ（主管）"},{"resultProp":["E","S","F","J"],"resultDesc":"友善热心，以协调、耐心和关怀为特点，善于团队合作。","resultPicture":"icon_url_esfj","resultName":"ESFJ（尽责者）"},{"resultProp":["E","N","F","J"],"resultDesc":"热情关爱，善于帮助他人，具有领导力和社交能力。","resultPicture":"icon_url_enfj","resultName":"ENFJ（教导着）"},{"resultProp":["E","N","T","J"],"resultDesc":"果断自信，具有领导才能，善于规划和执行目标。","resultPicture":"icon_url_entj","resultName":"ENTJ（统帅）"}]');

/***/ })

},
/******/ function(__webpack_require__) { // webpackRuntimeModules
/******/ var __webpack_exec__ = function(moduleId) { return __webpack_require__(__webpack_require__.s = moduleId); }
/******/ __webpack_require__.O(0, ["taro","vendors","common"], function() { return __webpack_exec__("./src/pages/result/index.tsx"); });
/******/ var __webpack_exports__ = __webpack_require__.O();
/******/ }
]);
//# sourceMappingURL=index.js.map