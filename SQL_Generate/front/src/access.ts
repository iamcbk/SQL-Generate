export default (initialState: InitialState) => {
  const canUser = !!initialState.loginUser;
  const canAdmin =
    initialState.loginUser && initialState.loginUser.userRole === 'admin';
  return {
    canUser,
    canAdmin,
  };
};
