$(function(){
	$("#isActiveSelect").select2({  
		placeholder: "----请选择----",
		allowClear: true
	}); 
	
	$("#isActive").select2({  
		placeholder: "----请选择----",
		allowClear: true
	}); 
	
	$("#bizTypeSelect").select2({  
		placeholder: "----请选择----",
		allowClear: true
	}); 
	
	$("#bizType").select2({  
		placeholder: "----请选择----",
		allowClear: true
	}); 
	$("#infTypeSelect").select2({  
		placeholder: "----请选择----",
		allowClear: true
	}); 
	
	$("#infType").select2({  
		placeholder: "----请选择----",
		allowClear: true
	}); 
	
	
	//------------------新增页面-----------------//
	
 
	$("#fieldTypeSelect").change(function(){
		//code...
			 
			if($('#fieldTypeSelect option:selected') .val()!=''){
				 
				if($('#fieldTypeSelect option:selected') .val()==0){
					 
					$("input").attr("disabled",false);					
				}else{
				
					$('#quickPayMaxMoney').val("");
					$('#collectMaxMoney').val("");
					$('#payMaxMoney').val("");
					$('#collectMaxMoney').attr("disabled","disabled");
					$('#payMaxMoney').attr("disabled","disabled");
					$('#quickPayMaxMoney').attr("disabled","disabled");
					
				}
			}else{
				
				$('#quickPayMaxMoney').val("");
				$('#collectMaxMoney').val("");
				$('#payMaxMoney').val("");
				$('#collectMaxMoney').attr("disabled","disabled");
				$('#payMaxMoney').attr("disabled","disabled");
				$('#quickPayMaxMoney').attr("disabled","disabled");
			}
			
		});
	 



});
//------------------列表页面-----------------//

/**
 * 条件查询所有记录
 */
function search(){
	var form = $("#threadPoolForm");
	form.submit();
}

/**
 * 查询条件重置
 */
function formReset(){
	$("#threadPoolForm input").val('');
	$("#threadPoolForm select").select2('val','');
}

/**
 * 跳转到修改页面
 */
function editUI(id){
	window.location.href = contextPath+"/bim/threadPool/threadPoolEditUI/"+id;
}
function updateFlag(id,active){
	
	window.location.href = contextPath+"/bim/threadPool/threadPoolUpdateIsActive/"+id+"/"+active;
}
/**
 * 批量删除
 */
function deleteList(){
	var checkedSize = $('input[name="subCheckBox"]:checked').size();
	if(checkedSize == 0){
		 $.teninedialog({
             title:'系统提示',
             content:'未选中任何记录！'
         });
	}else{
		$.teninedialog({
            title:'系统提示',
            content:'确定要删除选中的所有记录？',
            otherButtons:["确定","取消"],
            showCloseButton:false,//是否显示关闭按钮
            otherButtonStyles:['btn-primary'],
            clickButton:function(sender,modal,index){
                if(index == 0){
              
                	
                		var form = $("#resultForm");
                		form.attr("action", contextPath+"/sym/permission/permsDelete"); 
                		form.submit();
                	
                }
                $(this).closeDialog(modal);
            }
        });
	}
}

/**
 * 查询角色(列表)中已分配给用户的数量
 */
function getPermUsedCount(){
	var permIds = '';
	$("input[name='subCheckBox']:checked").each(function(){
		permIds += $(this).attr('value') + ",";
	});
	permIds = permIds.substring(0, permIds.length-1);
	var count = 0;
	$.ajax({
        type: "post",
        url: contextPath+"/sym/permission/getPermUsedCount",
        data: {"permIds":permIds},
        dataType: "json",
        async: false,
        success: function(data){
            if(data && data>0){
            	count = data;
            }
        }
    });
	return count;
};

//------------------新增页面-----------------//
/**
 * 取消按钮事件
 */
function cancel() {
   window.location.href = contextPath+"/bim/threadPool/threadPoolList";
}
