import { message } from 'antd';
import { Navigate, Outlet, useAccess } from 'umi';

export default () => {
  const { canUser } = useAccess();
  if (canUser) {
    return <Outlet />;
  } else {
    message.warning('请先登录');
    return <Navigate to="/user/login" replace />;
  }
};
