interface PageInfo<T> {
  current: number;
  size: number;
  total: number;
  records: T[];
}

interface PageRequest {
  current?: number;
  pageSize?: number;
  sortField?: string;
  sortOrder?: 'ascend' | 'descend';
}

interface DeleteRequest {
  id: number;
}

interface BaseResponse<T> {
  code: number;
  data: T;
  message?: string;
}

interface InitialState {
  loginUser?: UserType.UserVO;
}
