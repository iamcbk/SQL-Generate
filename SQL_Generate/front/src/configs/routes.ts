export default [
  {
    name: '代码生成',
    path: '/',
    component: 'index',
  },
  {
    name: '词库大全',
    path: '/dict/all',
    component: 'dict',
  },
  {
    name: '表大全',
    path: '/table/all',
    component: 'tableInfo',
  },
  {
    name: '字段大全',
    path: '/field/all',
    component: 'fieldInfo',
  },
  {
    name: '创建词库',
    path: '/dict/add',
    component: 'dict/add',
    wrappers: [
      '@/wrappers/auth',
    ],
  },
  {
    name: '创建词库成功',
    path: '/dict/add_result',
    component: 'dict/add_result',
    hideInMenu: true,
    wrappers: [
      '@/wrappers/auth',
    ],
  },
  {
    path: '/user',
    hideInMenu: true,
    headerRender: false,
    routes: [
      {
        name: '用户登录',
        path: '/user/login',
        component: 'user/login',
      },
      {
        name: '用户注册',
        path: '/user/register',
        component: 'user/register',
      },
    ],
  },
  {
    path: '/admin',
    access: 'canAdmin',
    name: '管理',
    // flatMenu: true,
    routes: [
      {
        name: '用户管理',
        path: '/admin/user',
        component: 'admin/user',
      },
      {
        name: '词库管理',
        path: '/admin/dict',
        component: 'admin/dict',
      },
      {
        name: '表管理',
        path: '/admin/table',
        component: 'admin/tableInfo',
      },
      {
        name: '字段管理',
        path: '/admin/field',
        component: 'admin/fieldInfo',
      },
      {
        name: '举报管理',
        path: '/admin/report',
        component: 'admin/report',
      },
    ],
  },
  {
    path: '/tableInfo',
    routes: [
      { path: '/tableInfo', component: './tableInfo/index' },
      { path: '/tableInfo/add', component: './tableInfo/add' },
    ],
  }
];