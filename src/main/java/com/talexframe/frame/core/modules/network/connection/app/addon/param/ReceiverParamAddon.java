package com.talexframe.frame.core.modules.network.connection.app.addon.param;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddonAdapter;
import com.talexframe.frame.core.modules.network.connection.app.subapp.MethodAppReceiver;
import com.talexframe.frame.core.pojo.wrapper.ResultData;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import com.talexframe.frame.utils.UrlUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
@Slf4j
public class ReceiverParamAddon extends ReceiverAddon {

    public ReceiverParamAddon() {

        super("ReceiverParam", new ReceiverAddonType[] { ReceiverAddonType.METHOD_APP });

    }

    @Override
    public boolean onPreInvokeMethod(MethodAppReceiver methodAppReceiver, WrappedResponse wr) {

        boolean parseData = methodAppReceiver.isParseData();

        String str = parseData ? wr.getRequest().getBody() : null;
        JSONObject json = null;

        if ( parseData ) {

            if( StrUtil.isBlankIfStr(str) ) {

                wr.returnDataByFailed(ResultData.ResultEnum.INFORMATION_ERROR, "Data error");

                log.info("[解析层] AccessDenied # MissingBodyData");

                return false;

            }

            json = JSONUtil.parseObj(str);

            log.debug("[解析层] RequestData: " + json.toString());

        }

        for ( Parameter parameter : methodAppReceiver.getMethod().getParameters() ) {

            log.debug("[解析层]  -- Parameter: " + parameter.getName());

            if( parameter.getType() == WrappedResponse.class ) {

                methodAppReceiver.getParams().add(wr);

                log.debug("[解析层]     #    -> WrappedResponse");

                continue;

            }

            if ( !this.processUrlParam( methodAppReceiver, parameter, wr ) ) {

                log.debug("[解析层]     #    -> UrlParam");

                continue;

            }

            if( !parseData ) continue;

            TParam tParam = parameter.getAnnotation(TParam.class);

            if( tParam == null ) {

                methodAppReceiver.getParams().add(null);

                log.warn("[解析层] AccessWarned # MissingParamAnnotation -> In null");

                continue;

            }

            log.debug("[解析层]     #    -> TParam");

            try {

                String fieldName = tParam.value() != null ? tParam.value() : parameter.getName();

                log.debug("[解析层]          -> FieldName: " + fieldName);

                if ( !json.containsKey(fieldName) ) {

                    if ( extracted(wr, methodAppReceiver, parameter, tParam) ) {

                        log.debug("[解析层]  -- # -> Extracted");

                        return false;

                    }

                } else {

                    Object obj = json.get(fieldName, parameter.getType());

                    log.debug("[解析层]  -- # -> Param: " + obj);

                    AtomicBoolean could = new AtomicBoolean(true);
                    LinkedList<ReceiverAddon> paramReceiverAddons = ReceiverAddonAdapter.getReceiverAddons(ReceiverAddonType.PARAM_APP);
                    paramReceiverAddons.forEach((addon) -> {

                        if( !could.get() ) return;

                        log.debug("[解析层]     Accessing -> {} | Object: {}", parameter.getType().getName(), obj);

                        could.set(addon.onPreAddParam(methodAppReceiver, parameter, wr, obj));

                        log.info("[解析层]     Accessing <- Value: {}", could.get());

                    });

                    methodAppReceiver.getParams().add(obj);

                    paramReceiverAddons.forEach((addon) -> {

                        log.debug("[解析层]     Accessing -> {} | Object: {}", parameter.getType().getName(), obj);

                        addon.onPostAddParam(methodAppReceiver, parameter, wr, obj);

                        log.debug("[解析层]     Accessing <- Done!");

                    });

                }

            } catch ( ConvertException e ) {

                if ( extracted(wr, methodAppReceiver, parameter, tParam) ) {

                    e.printStackTrace();

                    return false;

                }

            }

        }

        return true;

    }

    // if ( validator != null ) {
    //
    //     int back = ValidatorUtil.validateData(validator, obj);
    //
    //     if( back != 100 ) {
    //
    //         wr.returnDataByFailed(ResultData.ResultEnum.INFORMATION_ERROR, "Data error");
    //
    //         switch ( back ) {
    //
    //             case ValidatorUtil.MISS_MAX:
    //                 log.info("[解析层] AccessDenied # MissingMax " + fieldName);
    //                 break;
    //             case ValidatorUtil.MISS_MIN:
    //                 log.info("[解析层] AccessDenied # MissingMin " + fieldName);
    //                 break;
    //             case ValidatorUtil.MISS_PATTERN:
    //                 log.info("[解析层] AccessDenied # MissingPattern " + fieldName);
    //                 break;
    //             case ValidatorUtil.MISS_MAX_LENGTH:
    //                 log.info("[解析层] AccessDenied # MissingMaxLength " + fieldName);
    //                 break;
    //             case ValidatorUtil.MISS_MIN_LENGTH:
    //                 log.info("[解析层] AccessDenied # MissingMinLength " + fieldName);
    //                 break;
    //             case ValidatorUtil.MISS_ASSERT:
    //                 log.info("[解析层] AccessDenied # MissingAssert " + fieldName);
    //                 break;
    //             case ValidatorUtil.MISS_DATA:
    //                 log.info("[解析层] AccessDenied # MissingData " + fieldName);
    //                 break;
    //
    //         }
    //
    //         return;
    //
    //     }
    //
    // }

    public boolean processUrlParam(MethodAppReceiver methodReceiver, Parameter parameter, WrappedResponse wr) {

        TUrlParam tUrlParam = parameter.getAnnotation(TUrlParam.class);

        if ( tUrlParam == null ) return true;

        String url = UrlUtil.formatUrl("/" + wr.getRequest().getRequestURI());
        String[] requestUrls = url.split("/");
        String requestUrl = UrlUtil.formatUrl("/" + methodReceiver.getTRequest().value());
        String[] requireUrls = requestUrl.split("/");

        if ( requestUrls.length != requireUrls.length ) {

            log.info("[解析层]   AccessDenied - missing parameters # " + url + " as " + requestUrl + " | {}, {}", requestUrls.length, requireUrls.length);

            wr.returnDataByFailed(ResultData.ResultEnum.INFORMATION_ERROR, "Data error!");

            return false;

        }

        String fieldName = tUrlParam.value();
        String[] originUrl = UrlUtil.formatUrl(requestUrl).split("/");
        String[] urls = UrlUtil.formatUrl(url).split("/");

        log.debug("[解析层] --> For method: {}", (Object) methodReceiver.getMethod().getParameters());

        for( int i = 0; i < originUrl.length; ++i ) {

            String thisUrl = originUrl[i];
            log.debug("[解析层]     thisUrl: " + thisUrl + " | fieldName: " + fieldName);

            if( thisUrl.equalsIgnoreCase("{" + fieldName + "}") ) {

                String obj = (i + 1 >= urls.length) ? urls[i] : urls[i + 1];

                int ind = methodReceiver.getParams().size();

                AtomicBoolean could = new AtomicBoolean(true);
                LinkedList<ReceiverAddon> urlParamReceiverAddons = ReceiverAddonAdapter.getReceiverAddons(ReceiverAddonType.URL_PARAM_APP);
                urlParamReceiverAddons.forEach((addon) -> {

                    if( !could.get() ) return;

                    could.set(addon.onPreAddUrlParam(methodReceiver, methodReceiver.getMethod().getParameters()[ind], wr, requestUrl, url, fieldName, obj));

                });

                methodReceiver.getParams().add(obj);

                urlParamReceiverAddons.forEach((addon) -> {

                    addon.onPostAddUrlParam(methodReceiver, methodReceiver.getMethod().getParameters()[ind], wr, requestUrl, url, fieldName, obj);

                });

                log.debug("[解析层]     add param: " + obj);

                return true;

            }

        }

        return false;

    }

    private boolean extracted(WrappedResponse wr, MethodAppReceiver methodAppReceiver, Parameter parameter, TParam param) {

        if ( !param.nullable() ) {

            wr.returnDataByFailed(ResultData.ResultEnum.INFORMATION_ERROR, "Parameter error");

            log.info("[接口层] 请求参数错误 - " + ( !StrUtil.isBlankIfStr(param.value()) ? param.value() : parameter.getName() ));

            return true;

        } else {

            methodAppReceiver.getParams().add(null);

        }

        return false;

    }

}
