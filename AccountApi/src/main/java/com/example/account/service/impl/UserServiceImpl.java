package com.example.account.service.impl;

import com.example.account.entity.House;
import com.example.account.entity.User;

import com.example.account.enums.ReturnCode;
import com.example.account.mapper.HouseMapper;
import com.example.account.mapper.UserMapper;
import com.example.account.param.BaseUserParam;
import com.example.account.param.RemoveUserParam;
import com.example.account.param.TransferUserParam;
import com.example.account.param.UpdateUserParam;
import com.example.account.response.PageResult;
import com.example.account.response.Result;
import com.example.account.service.UserService;
import com.example.account.service.EmailService;
import com.example.account.utils.MD5Utils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private EmailService emailService;

    private static final int DEFAULT_PASSWORD_LENGTH = 8;
    private static final String CHAR_LIST = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result register(BaseUserParam param) {
        try {
            // 验证参数
            if (!validateRegisterParams(param)) {
                return Result.failure(ReturnCode.PARAM_ERROR);
            }

            // 验证验证码
            if (!emailService.verifyCode(param.getEmail(), param.getVerificationCode())) {
                return Result.failure(ReturnCode.VERIFICATION_CODE_ERROR);
            }

            // 验证邮箱是否已存在
            if (isEmailExists(param.getEmail())) {
                return Result.failure(ReturnCode.EMAIL_EXISTS);
            }

            // 验证用户名是否已存在
            if (isUserNameExists(param.getName())) {
                return Result.failure(ReturnCode.REGISTER_FAIL);
            }

            // 创建家庭
            House house = createNewHouse(param);
            
            // 创建用户
            param.setHouseId(house.getId());
            param.setIsAdmin(1);
            addNewUser(param);

            return Result.success();
        } catch (Exception e) {
            log.error("Register user failed: ", e);
            throw e;
        }
    }

    @Override
    public Result addUser(BaseUserParam param) {
        try {
            // 验证管理员权限
            if (!isAdmin(param.getId())) {
                return Result.failure(ReturnCode.PERMISSION_DENIED);
            }

            // 验证参数
            if (!validateAddUserParams(param)) {
                return Result.failure(ReturnCode.PARAM_ERROR);
            }

            // 验证邮箱是否已存在
            if (isEmailExists(param.getEmail())) {
                return Result.failure(ReturnCode.EMAIL_EXISTS);
            }

            // 验证用户名是否已存在
            if (isUserNameExists(param.getName())) {
                return Result.failure(ReturnCode.REGISTER_FAIL);
            }

            User user = addNewUser(param);
            return Result.success(user);
        } catch (Exception e) {
            log.error("Add user failed: ", e);
            throw e;
        }
    }

    @Override
    public Result queryUser(BaseUserParam param) {
        try {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<User> list = userMapper.selectByHouseId(param.getHouseId());
            PageInfo<User> pageInfo = new PageInfo<>(list);
            return Result.success(new PageResult(pageInfo.getTotal(), pageInfo.getList()));
        } catch (Exception e) {
            log.error("Query users failed: ", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result removeUser(RemoveUserParam param) {
        try {
            // 验证管理员权限
            if (!isAdmin(param.getAdminUserId())) {
                return Result.failure(ReturnCode.PERMISSION_DENIED);
            }

            userMapper.removeUser(param.getRemoveUserId());
            return Result.success();
        } catch (Exception e) {
            log.error("Remove user failed: ", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result transferAdmin(TransferUserParam param) {
        try {
            User fromUser = userMapper.selectByPrimaryKey(param.getFromUserId());
            User toUser = userMapper.selectByPrimaryKey(param.getToUserId());

            // 验证转移权限
            if (!validateTransferAdmin(fromUser, toUser)) {
                return Result.failure(ReturnCode.PERMISSION_DENIED);
            }

            // 更新用户权限
            updateAdminRights(param.getFromUserId(), param.getToUserId(), toUser);
            return Result.success();
        } catch (Exception e) {
            log.error("Transfer admin failed: ", e);
            throw e;
        }
    }

    @Override
    public Result resetPassword(BaseUserParam param) {
        try {
            // 验证管理员权限
            if (!isAdmin(param.getId())) {
                return Result.failure(ReturnCode.PERMISSION_DENIED);
            }

            String newPassword = generateRandomPassword();
            User record = new User();
            record.setPassword(MD5Utils.EncoderByMd5(newPassword));
            record.setId(param.getTargetUserId());
            record.setUpdateTime(new Date());

            int result = userMapper.updateByPrimaryKeySelective(record);
            if (result <= 0) {
                return Result.failure(ReturnCode.RESET_PASSWORD_FAIL);
            }

            record.setPassword(newPassword);
            return Result.success(record);
        } catch (Exception e) {
            log.error("Reset password failed: ", e);
            throw e;
        }
    }

    @Override
    public Result resetPasswordByEmail(BaseUserParam param) {
        try {
            // 验证验证码
            if (!emailService.verifyCode(param.getEmail(), param.getVerificationCode())) {
                return Result.failure(ReturnCode.VERIFICATION_CODE_ERROR);
            }

            // 查找用户
            User user = findUserByEmail(param.getEmail());
            if (user == null) {
                return Result.failure(ReturnCode.EMAIL_NOT_EXISTS);
            }

            // 更新密码
            user.setPassword(MD5Utils.EncoderByMd5(param.getNewPassword()));
            user.setUpdateTime(new Date());

            int result = userMapper.updateByPrimaryKeySelective(user);
            if (result <= 0) {
                return Result.failure(ReturnCode.RESET_PASSWORD_FAIL);
            }

            return Result.success(user.getName());
        } catch (Exception e) {
            log.error("Reset password by email failed: ", e);
            throw e;
        }
    }

    @Override
    public Result loginUser(BaseUserParam param) {
        try {
            String password = MD5Utils.EncoderByMd5(param.getPassword());
            List<User> users = findUserByNameAndPassword(param.getName(), password);
            
            if (users.isEmpty()) {
                return Result.failure(ReturnCode.LOGIN_FAIL);
            }

            users.get(0).setPassword(null);
            return Result.success(users);
        } catch (Exception e) {
            log.error("User login failed: ", e);
            throw e;
        }
    }

    @Override
    public Result checkEmail(String email) {
        try {
            return Result.success(isEmailExists(email) ? "exists" : "not_exists");
        } catch (Exception e) {
            log.error("Check email failed: ", e);
            throw e;
        }
    }

    @Override
    public boolean isAdmin(Integer id) {
        try {
            User user = userMapper.selectByPrimaryKey(id);
            return user != null && user.getIsAdmin() == 1 && user.getIsDelete() == 0;
        } catch (Exception e) {
            log.error("Check admin permission failed: ", e);
            return false;
        }
    }

    @Override
    public Result updateMyPsw(UpdateUserParam param) {
        try {
            // 验证参数
            if (StringUtils.isEmpty(param.getName()) || 
                StringUtils.isEmpty(param.getOldPassword()) || 
                StringUtils.isEmpty(param.getNewPassword())) {
                return Result.failure(ReturnCode.PARAM_ERROR);
            }

            // 验证原密码
            String oldPasswordMd5 = MD5Utils.EncoderByMd5(param.getOldPassword());
            User user = userMapper.selectByNameAndPasswordForUpdate(param.getName(), oldPasswordMd5);
            if (user == null) {
                return Result.failure(ReturnCode.LOGIN_FAIL);
            }

            // 更新密码
            UpdateUserParam updateParam = new UpdateUserParam();
            updateParam.setId(user.getId());
            updateParam.setNewPassword(MD5Utils.EncoderByMd5(param.getNewPassword()));
            
            int result = userMapper.updateMyPsw(updateParam);
            if (result <= 0) {
                return Result.failure(ReturnCode.UPDATE_PASSWORD_FAIL);
            }

            return Result.success();
        } catch (Exception e) {
            log.error("Update password failed: ", e);
            throw e;
        }
    }

    private boolean validateRegisterParam(BaseUserParam param) {
        return !StringUtils.isEmpty(param.getName()) &&
               !StringUtils.isEmpty(param.getPassword()) &&
               !StringUtils.isEmpty(param.getEmail());
    }

    // 私有辅助方法
    private boolean validateRegisterParams(BaseUserParam param) {
        return !StringUtils.isEmpty(param.getName()) &&
               !StringUtils.isEmpty(param.getPassword()) &&
               !StringUtils.isEmpty(param.getEmail()) &&
               !StringUtils.isEmpty(param.getVerificationCode());
    }

    private boolean validateAddUserParams(BaseUserParam param) {
        return !StringUtils.isEmpty(param.getName()) &&
               !StringUtils.isEmpty(param.getPassword());
    }

    private boolean validateUpdatePasswordParam(UpdateUserParam param) {
        return param != null && 
               param.getId() != null && 
               !StringUtils.isEmpty(param.getOldPassword()) && 
               !StringUtils.isEmpty(param.getNewPassword());
    }

    private boolean validateResetPasswordParam(BaseUserParam param) {
        return param.getId() != null &&
               param.getTargetUserId() != null &&
               !StringUtils.isEmpty(param.getNewPassword());
    }

    private boolean validateRemoveUser(User fromUser, User toUser) {
        return fromUser != null &&
               toUser != null &&
               fromUser.getIsAdmin() == 1 &&
               toUser.getHouseId().equals(fromUser.getHouseId()) &&
               toUser.getIsAdmin() == 0;
    }

    private boolean validateEmailResetPasswordParam(BaseUserParam param) {
        return !StringUtils.isEmpty(param.getEmail()) &&
               !StringUtils.isEmpty(param.getVerificationCode()) &&
               !StringUtils.isEmpty(param.getNewPassword());
    }

    private boolean isEmailExists(String email) {
        return userMapper.selectByEmail(email) != null;
    }

    private boolean isUserNameExists(String name) {
        return userMapper.selectByName(name) != null;
    }

    private House createNewHouse(BaseUserParam param) {
        House house = new House();
        house.setAdminName(param.getName());
        house.setCreateTime(new Date());
        house.setUpdateTime(new Date());
        house.setBudget(param.getBudget());
        house.setIsDelete(0);
        houseMapper.insert(house);
        
        return houseMapper.selectByAdminName(param.getName());
    }

    private User addNewUser(BaseUserParam param) {
        Date now = new Date();
        User user = new User();
        user.setName(param.getName());
        user.setPassword(MD5Utils.EncoderByMd5(param.getPassword()));
        user.setIsAdmin(param.getIsAdmin());
        user.setHouseId(param.getHouseId());
        user.setEmail(param.getEmail());
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setIsDelete(0);
        userMapper.insertSelective(user);
        return user;
    }

    private boolean validateTransferAdmin(User fromUser, User toUser) {
        return fromUser != null && 
               toUser != null && 
               fromUser.getIsAdmin() == 1 && 
               toUser.getHouseId().equals(fromUser.getHouseId());
    }

    private void updateAdminRights(Integer fromUserId, Integer toUserId, User toUser) {
        userMapper.transferAdmin(fromUserId, 0);
        userMapper.transferAdmin(toUserId, 1);

        House record = new House();
        record.setId(toUser.getHouseId());
        record.setAdminName(toUser.getName());
        record.setUpdateTime(new Date());
        houseMapper.updateByPrimaryKeySelective(record);
    }

    private List<User> findUserByNameAndPassword(String name, String password) {
        return userMapper.selectByNameAndPassword(name, password);
    }

    private User findUserByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    private String generateRandomPassword() {
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < DEFAULT_PASSWORD_LENGTH; i++) {
            password.append(CHAR_LIST.charAt(random.nextInt(CHAR_LIST.length())));
        }
        return password.toString();
    }
}
