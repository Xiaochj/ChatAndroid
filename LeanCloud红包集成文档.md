# LeanCloud红包集成文档


## 1. 红包库简介

**红包库使用远程依赖的方式**，在LeanCloud **SDK** 的基础上提供了收发红包和零钱页的功能。

## 2. 红包相关文件说明

* libs ：包含了红包所需要的jar包。

  * alipaySdk-20160516支付宝支付
  * glide-3.6.1图片加载库
  * volley-1.0.19请求框架
  * libammsdk微信支付

* res ：包含了红包SDK和聊天页面中的资源文件。（红包SDK相关以lc_开头）

* redpacket ：此包包含红包发送接收的工具类

  * GetSignInfoCallback 获取签名接口回调
  * GetGroupMemberCallback 获取群里面的人数（app开发者需要自己处理）的接口回调。
  * RedPacketUtils 发送打开红包相关的工具类

* message ：

  * LCIMRedPacketMessage 自定义红包消息
  * LCIMRedPcketAckMessage 自定义通知消息，用于领取了红包之后，回执消息发给红包者。  **用于群/个人领取了红包之后，1、接受者先向本地插入一条“你领取了XX的红包”，然后发送一条空消息（不在聊天界面展示），发送红包者收到消息之后，向本地插入一条“XX领取了你的红包”，2、如果接受者和发送者是一个人就直接向本地插入一条“你领取了自己的红包”**
  * InputRedPacketClickEvent 红包按钮点击事件

* viewholder ：

  * ChatItemRedPacketHolder 红包消息处理机制
  * ChatItemRedPacketAckHolder 回执消息UI展示提供者
  * ChatItemRedPacketEmptyHolder 空消息用于隐藏和自己不相关的消息

  **注意: redpacketlibrary-aar只支持AndroidStudio**。

  **注意: 拆红包添加音效，需要在assets中引入open_packet_sound.mp3(文件名必须为此名，暂时支持mp3和wav格式)**。

## 3. 集成步骤

###3.1 添加对红包工程的依赖
* leanchat-android的build.gradle中

```java
    compile files('libs/alipaySdk-20160516.jar')
    compile files('libs/glide-3.6.1.jar')
    compile files('libs/volley-1.0.19.jar')
    compile files('libs/libammsdk.jar')
    compile('com.yunzhanghu:redpacket:3.4.0@aar')
    allprojects {
    repositories {
        jcenter()
        maven {
            url "https://raw.githubusercontent.com/YunzhanghuOpen/redpacket-maven-repo/dev/snapshots"
        }
    }
}
```

* leanchat-android的setting.gradle中

```java
    include ':leanchat'
```
### 3.2 leanchat-android清单文件中注册红包相关组件

```java

    <uses-sdk
        android:minSdkVersion="15"/>
 
    <!--红包相关界面 start-->
    <activity
            android:name="com.yunzhanghu.redpacketui.ui.activity.RPRedPacketActivity"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustPan|stateVisible" />
    <activity
            android:name="com.yunzhanghu.redpacketui.ui.activity.RPDetailActivity"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustPan" />

    <activity
            android:name="com.yunzhanghu.redpacketui.ui.activity.RPRecordActivity"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustPan" />

    <activity
            android:name="com.yunzhanghu.redpacketui.ui.activity.RPWebViewActivity"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustResize|stateHidden" />
    <activity
            android:name="com.yunzhanghu.redpacketui.ui.activity.RPChangeActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustResize|stateHidden" />
    <activity
            android:name="com.yunzhanghu.redpacketui.ui.activity.RPBankCardActivity"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustPan|stateHidden" />
    <activity
            android:name="com.yunzhanghu.redpacketui.ui.activity.RPGroupMemberActivity"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustPan|stateHidden" />
    <activity
            android:name="com.alipay.sdk.app.H5PayActivity"
            android:configChanges="orientation|keyboardHidden|navigation|screenSize"
            android:exported="false"
            android:screenOrientation="behind"
            android:windowSoftInputMode="adjustResize|stateHidden" />
     <!-- 转账页面 -->
     <activity
            android:name="com.yunzhanghu.redpacketui.ui.activity.RPTransferActivity"
            android:screenOrientation="portrait"
            android:theme="@style/horizontal_slide"
            android:windowSoftInputMode="adjustPan|stateVisible"/>
      <!-- 转账详情页面 -->
      <activity
            android:name="com.yunzhanghu.redpacketui.ui.activity.RPTransferDetailActivity"
            android:screenOrientation="portrait"
            android:theme="@style/horizontal_slide"
            android:windowSoftInputMode="adjustPan|stateHidden"/>
       <!-- 微信支付回调页面 -->
       <activity
            android:name="com.avoscloud.chat.wxapi.WXPayEntryActivity"
            android:exported="true"
            android:launchMode="singleTop"/>
    <!--红包相关界面 end-->
```
### 3.3 初始化红包上下文

* App中初始化红包上下文。

```java
    import com.yunzhanghu.redpacketsdk.RedPacket;
    import com.yunzhanghu.redpacketui.RedPacketUtil;
    
    @Override
    public void onCreate() {
        // 初始化红包操作
        RedPacket.getInstance().initRedPacket(ctx,RPConstant.AUTH_METHOD_SIGN,  new                                                                             
                                              RPInitRedPacketCallback() {       
      @Override
      public void initTokenData(final RPValueCallback<TokenData> rpValueCallback) {
           RedPacketUtils.getInstance().getRedPacketSign(ctx, new                                          
                                                         GetSignInfoCallback() {
          @Override
          public void signInfoSuccess(TokenData tokenData) {
            rpValueCallback.onSuccess(tokenData);
          }

          @Override
          public void signInfoError(String errorMsg) {
          }
        });
      }

      @Override
      public RedPacketInfo initCurrentUserSync() {
        //这里需要同步设置当前用户id、昵称和头像url
        RedPacketInfo redPacketInfo = new RedPacketInfo();
        redPacketInfo.fromUserId = LeanchatUser.getCurrentUserId();
        redPacketInfo.fromAvatarUrl = LeanchatUser.getCurrentUser().getAvatarUrl();
        redPacketInfo.fromNickName = LeanchatUser.getCurrentUser().getUsername();
        return redPacketInfo;
      }
    });
        // 控制红包SDK中Log打印
        RedPacket.getInstance().setDebugMode(true);
      	// 注册红包相关消息
        AVIMMessageManager.registerAVIMMessageType(LCIMRedPacketMessage.class);
        AVIMMessageManager.registerAVIMMessageType(LCIMRedPacketAckMessage.class);
        AVIMMessageManager.registerAVIMMessageType(LCIMTransferMessage.class);
    }
```

### 3.4 ProfileFragment添加零钱页的入口

* 在需要添加零钱的页面调用下面的方法

```java
    RPChangeActivity（零钱页面）
      
    RPRedPacketUtil.getInstance().startChangeActivity(getActivity());

```

## 4. 关于群、单聊红包的释义

### 4.1 ConversationFragment中、在扩展栏中增加红包按钮

* 增加按钮的方法参考addRedPacketView 

* 添加单聊红包入口

```java
单聊发红包页面RPRedPacketActivity（itemType为RPConstant.RP_ITEM_TYPE_SINGLE，群聊为RPConstant.RP_ITEM_TYPE_GROUP），需要参数有上下文，imConversation，itemType，接收者id（单聊传接收者id,群聊传群id）。
实例如下所示

    public void startRedPacket(final FragmentActivity activity, final AVIMConversation 
                               imConversation, final int itemType, final String toUserId, final 
                               RPSendPacketCallback callback) {
    final RedPacketInfo redPacketInfo = new RedPacketInfo();
    if (itemType == RPConstant.RP_ITEM_TYPE_GROUP) {
      /**
       * 发送专属红包用的,获取群组成员
       */
      RedPacket.getInstance().setRPGroupMemberListener(new RPGroupMemberListener() {
        @Override
        public void getGroupMember(String s, final RPValueCallback<List<RPUserBean>> 
                                   rpValueCallback) {
          initRpGroupMember(imConversation.getMembers(), new GetGroupMemberCallback() {
            @Override
            public void groupInfoSuccess(List<RPUserBean> rpUserList) {
              rpValueCallback.onSuccess(rpUserList);
            }

            @Override
            public void groupInfoError() {

            }
          });
        }
      });
      imConversation.getMemberCount(new AVIMConversationMemberCountCallback() {
        @Override
        public void done(Integer integer, AVIMException e) {
          redPacketInfo.toGroupId = imConversation.getConversationId();
          redPacketInfo.groupMemberCount = integer;
          RPRedPacketUtil.getInstance().startRedPacket(activity, itemType, redPacketInfo, 
                                                       callback);
        }
      });
    } else {
      redPacketInfo.toUserId = toUserId;
      LeanchatUser leanchatUser = UserCacheUtils.getCachedUser(toUserId);
      if (leanchatUser != null) {
        redPacketInfo.toNickName = TextUtils.isEmpty(leanchatUser.getUsername()) ? "none" : 
        leanchatUser.getUsername();
        redPacketInfo.toAvatarUrl = TextUtils.isEmpty(leanchatUser.getAvatarUrl()) ? "" : 
        leanchatUser.getAvatarUrl();
      }
      RPRedPacketUtil.getInstance().startRedPacket(activity, itemType, redPacketInfo, callback);
    }
  }     
```

* 发送红包之后数据展示

```java
  /**
   * 红包消息的数据
   */
  public LCIMRedPacketMessage createRPMessage(Context context, RedPacketInfo redPacketInfo) {
    String selfName = LeanchatUser.getCurrentUser().getUsername();
    String selfID = LeanchatUser.getCurrentUserId();

    LCIMRedPacketMessage redPacketMessage = new LCIMRedPacketMessage();
    redPacketMessage.setGreeting(redPacketInfo.redPacketGreeting);
    redPacketMessage.setRedPacketId(redPacketInfo.redPacketId);
    redPacketMessage.setSponsorName(context.getResources().
                                    getString(R.string.leancloud_luckymoney));
    redPacketMessage.setRedPacketType(redPacketInfo.redPacketType);
    redPacketMessage.setReceiverId(redPacketInfo.toUserId);
    redPacketMessage.setMoney(true);
    redPacketMessage.setSenderName(selfName);
    redPacketMessage.setSenderId(selfID);
    return redPacketMessage;
  }
       
```

### 4.2 ChatItemRedPacketHolder中打开红包消息和回执消息处理

* 接受红包消息打开红包

```java

打开红包需要调用RPOpenPacketUtil.getInstance().openRedPacket()方法，需要传的参数为用户名(selfName)，头像(selfAvatar)，是发送者还是接收者(moneyMsgDirect)，是单聊还是群聊(chatType)；是专属红包需要再多加3个字段，接收者的用户名(specialNickname)和头像(specialAvatarUrl)，以及发送者的id(toUserId)。
实例如下：

   private void openRedPacket(final Context context, final LCIMRedPacketMessage message) {
    final ProgressDialog progressDialog = new ProgressDialog(context);
    progressDialog.setCanceledOnTouchOutside(false);

    int chatType;
    if (!TextUtils.isEmpty(message.getRedPacketType())) {
      chatType = RPConstant.CHATTYPE_GROUP;
    } else {
      chatType = RPConstant.CHATTYPE_SINGLE;
    }

    RPRedPacketUtil.getInstance().openRedPacket(wrapperRedPacketInfo(chatType, message),
            (FragmentActivity) context,
            new RPRedPacketUtil.RPOpenPacketCallback() {
              @Override
              public void onSuccess(String senderId, String senderNickname, String myAmount) {
                 String selfName = LeanchatUser.getCurrentUser().getUsername();
                 String selfId = LeanchatUser.getCurrentUserId();
                 RedPacketUtil.getInstance().sendRedPacketAckMsg(senderId, senderNickname, selfId,     
                                                                 selfName, message);
              }

              @Override
              public void showLoading() {
                progressDialog.show();
              }

              @Override
              public void hideLoading() {
                progressDialog.dismiss();
              }

              @Override
              public void onError(String code, String message) { /*错误处理*/
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
              }
            });
  }

  /**
   * 封装拆红包所需参数
   *
   * @param chatType 聊天类型
   * @param message  EMMessage
   * @return RedPacketInfo
   */
  private RedPacketInfo wrapperRedPacketInfo(int chatType, LCIMRedPacketMessage message) {
    String redPacketId = message.getRedPacketId();
    String redPacketType = message.getRedPacketType();
    RedPacketInfo redPacketInfo = new RedPacketInfo();
    redPacketInfo.redPacketId = redPacketId;
    redPacketInfo.moneyMsgDirect = getMessageDirect(message);
    redPacketInfo.chatType = chatType;
    redPacketInfo.redPacketType = redPacketType;
    //3.4.0版之前集成过红包的用户，需要增加如下参数的传入对旧版本进行兼容
    if (!TextUtils.isEmpty(redPacketType) && redPacketType.equals(
            RPConstant.GROUP_RED_PACKET_TYPE_EXCLUSIVE)) {
      /**
       * 打开专属红包需要多传一下的参数
       */
      redPacketInfo.specialNickname = 
        TextUtils.isEmpty(UserCacheUtils.getCachedUser(message.getReceiverId()).getUsername()) ? 
        "" : UserCacheUtils.getCachedUser(message.getReceiverId()).getUsername();
      redPacketInfo.specialAvatarUrl = 
        TextUtils.isEmpty(UserCacheUtils.getCachedUser(message.getReceiverId()).getAvatarUrl()) ? 
        "none" : UserCacheUtils.getCachedUser(message.getReceiverId()).getAvatarUrl();
    }
    //兼容end
    return redPacketInfo;
  }

  private String getMessageDirect(LCIMRedPacketMessage message) {
    String selfId = LeanchatUser.getCurrentUserId();
    String moneyMsgDirect; /*判断发送还是接收*/
    if (message.getFrom() != null && message.getFrom().equals(selfId)) {
      moneyMsgDirect = RPConstant.MESSAGE_DIRECT_SEND;
    } else {
      moneyMsgDirect = RPConstant.MESSAGE_DIRECT_RECEIVE;
    }
    return moneyMsgDirect;
  } 
```
* 接受红包消息之后会话列表回执消息的处理 LCIMRedPcketAckMessage

```java
  @Override
  public String getShorthand() {
    String userId=LeanchatUser.getCurrentUserId();
    if (userId.equals(senderId)&&userId.equals(recipientId)){
      return "你领取了自己的红包";
    }else if (userId.equals(senderId)&&!userId.equals(recipientId)){
      return recipientName+"领取了你的红包";
    }else if (!userId.equals(senderId)&&userId.equals(recipientId)){
      return "你领取了"+senderName+"的红包";
    }else if (!userId.equals(senderId)&&!userId.equals(recipientId)){
      if (senderId.equals(recipientId)){
        return recipientName+"领取了自己的红包";
      }else {
        return recipientName+"领取了"+senderName+"的红包";
      }
    }
    return null;
  }
    
```
* 接受红包消息之后会话详情回执消息的处理ChatItemRedPacketAckHolder

```java
   /**
   * @param senderName    红包发送者名字
   * @param recipientName 红包接收者名字
   * @param isSelf        是不是自己领取了自己的红包
   * @param isSend        消息是不是自己发送的
   * @param isSingle      是单聊还是群聊
   */
    private void initRedPacketAckChatItem(String senderName, String   
          recipientName, boolean isSelf, boolean isSend, boolean     
          isSingle) {
      if (isSend) {
        if (!isSingle) {
          if (isSelf) {
            contentView.setText(R.string.money_msg_take_money);
          } else {           
             contentView.setText(String.format(getContext().           
             getResources().
             getString(R.string.money_msg_take_someone_money),     
             senderName));
          }
        } else {
             contentView.setText(String.format(getContext().
             getResources().
             getString(R.string.money_msg_take_someone_money),  
             senderName));
        }
      } else {
        if (isSelf) {
             contentView.setText(String.format(getContext().
             getResources().
             getString(R.string.money_msg_someone_take_money),     
             recipientName));
        } else {
             contentView.setText(String.format(getContext().
             getResources().
             getString(R.string.money_msg_someone_take_money_same),    
             recipientName, senderName));
        }
      }
    }
    
```

### 4.3 ChatAdapter中处理红包消息

```java
   /**
   * 判断是什么消息类型
   */

  @Override
  public int getItemViewType(int position) {
    AVIMMessage message = messageList.get(position);
    if (null != message && message instanceof AVIMTypedMessage) {
      AVIMTypedMessage typedMessage = (AVIMTypedMessage) message;
      boolean isMe = fromMe(typedMessage);
      if (typedMessage.getMessageType() == LCIMRedPacketMessage.RED_PACKET_MESSAGE_TYPE) {
        return isMe ? ITEM_RIGHT_TEXT_RED_PACKET : ITEM_LEFT_TEXT_RED_PACKET;
      } else if (typedMessage.getMessageType() ==       
                 LCIMRedPacketAckMessage.RED_PACKET_ACK_MESSAGE_TYPE) {
       return      RedPacketUtils.getInstance().receiveRedPacketAckMsg((LCIMRedPacketAckMessage)                                                                 typedMessage,ITEM_TEXT_RED_PACKET_NOTIFY,ITEM_TEXT_RED_PACKET_NOTIFY_MEMBER);
      }
    }
    return super.getItemViewType(position);
  }
```





