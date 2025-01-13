// 全局运行时配置
import Logo from '@/assets/logo.png';
import GlobalFooter from '@/components/GlobalFooter';
import { getLoginUser } from '@/services/userService';
import { RunTimeLayoutConfig } from '@@/plugin-layout/types';
import type { RequestConfig } from 'umi';
import './global.less';
import RightContent from '@/components/GlobalHeader/RightContent';

export async function getInitialState(): Promise<InitialState> {
  const defaultState: InitialState = {
    loginUser: undefined,
  };
  // 获取当前登录用户
  try {
    const res = await getLoginUser();
    defaultState.loginUser = res.data;
  } catch (e) {}
  return defaultState;
}

export const layout: RunTimeLayoutConfig = () => {
  return {
    title: 'SQL Generate Tool',
    logo: Logo,
    menu: {
      locale: false,
    },
    fixedHeader: false,
    layout: 'top',
    contentStyle: {
      paddingBottom: 120,
    },
    rightContentRender: () => <RightContent />,
    footerRender: () => <GlobalFooter />,
  };
};

const isDev = process.env.NODE_ENV === 'development';

export const request: RequestConfig = {
  baseURL: isDev ? 'http://localhost:8080/api' : '你的线上接口地址',
  timeout: 10000,
  withCredentials: true,
  errorConfig: {
    errorHandler() {},
    errorThrower() {},
  },
  requestInterceptors: [],
  responseInterceptors: [
    (response) => {
      const data: any = response.data;
      const path = response.request.responseURL;
      if (!data) {
        throw new Error('服务异常');
      }

      if (path.includes('download/data/excel')) {
        return response;
      }
      const code = data.code ?? 50000;

      if (
        code === 40100 &&
        !path.includes('user/get/login') &&
        !location.pathname.includes('/user/login')
      ) {
        window.location.href = `/user/login?redirect=${window.location.href}`;
        throw new Error('请先登录');
      }
      if (code !== 0) {
        console.error(`request error, path = ${path}`, data);
        throw new Error(data.message ?? '服务器错误');
      }
      return response;
    },
  ],
};
